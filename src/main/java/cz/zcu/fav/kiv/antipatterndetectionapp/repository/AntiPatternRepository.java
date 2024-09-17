package cz.zcu.fav.kiv.antipatterndetectionapp.repository;

import com.fasterxml.jackson.databind.JsonNode;
import cz.zcu.fav.kiv.antipatterndetectionapp.detecting.detectors.AntiPatternDetector;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.AntiPattern;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Threshold;
import cz.zcu.fav.kiv.antipatterndetectionapp.utils.JsonParser;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;

/**
 * A class that takes care of working with AP.
 */
@Component
public class AntiPatternRepository {

    private final String QUERY_DIR;
    private final String AP_DIR;
    private final String OPERATIONALIZATION_DIR;
    private final String OPERATIONALIZATION_IMG_DIR;
    private final Logger LOGGER = LoggerFactory.getLogger(AntiPatternRepository.class);
    private Map<Long, AntiPatternDetector> antiPatternDetectors;


    public AntiPatternRepository(@Value("${DATA_PATH}")String dir){
        this.OPERATIONALIZATION_DIR = dir + "operationalizations/";
        this.OPERATIONALIZATION_IMG_DIR = dir + "operationalizations/images/";
        this.AP_DIR = "antipatterns/";
        this.QUERY_DIR = "queries/";

        this.antiPatternDetectors = initDetectors();
        initSqlQueries();
    }

    /**
     * The method that loads all available detectors that implement
     * the AntiPatternDetector interface creates objects that the
     * application continues to work with. Detection of all interface
     * implementations is performed using reflection.
     *
     * @return map of all AP
     */
    private Map<Long, AntiPatternDetector> initDetectors() {
        LOGGER.info("-------START CREATING DETECTORS WITH REFLECTION-------");

        Map<Long, AntiPatternDetector> antiPatterns = new HashMap<>();
        try {
            Reflections reflections = new Reflections("cz.zcu.fav.kiv.antipatterndetectionapp");
            Set<Class<? extends AntiPatternDetector>> subTypes = reflections.getSubTypesOf(AntiPatternDetector.class);
            for (Class<? extends AntiPatternDetector> subType : subTypes) {
                AntiPatternDetector antiPatternDetector = subType.getDeclaredConstructor().newInstance();

                // loading anti-pattern from json file and linking it to the detector file
                AntiPattern antiPattern = getAntiPatternFromJsonFile(antiPatternDetector.getJsonFileName());

                // anti-pattern was not read from file properly
                if(antiPattern == null) {
                    LOGGER.error("Anti-pattern from " + antiPatternDetector.getJsonFileName() + " was not read correctly");
                    continue;
                }
                antiPatternDetector.setAntiPattern(antiPattern);

                antiPatterns.putIfAbsent(antiPatternDetector.getAntiPatternModel().getId(), antiPatternDetector);
                LOGGER.info("Creating detector " + antiPatternDetector.getAntiPatternModel().getPrintName());
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            LOGGER.error("Cannot get all detectors with reflection", e);
        }

        LOGGER.info("-------FINISHED CREATING DETECTORS WITH REFLECTION-------");

        return antiPatterns;
    }

    /**
     * Method for reading SQL queries from files
     */
    private void initSqlQueries(){
        LOGGER.info("-------START READING SQL FROM FILES-------");

        for (AntiPatternDetector antiPatternDetector : getAllAntiPatterns()) {
            LOGGER.info("Reading sql files for AP " + antiPatternDetector.getAntiPatternModel().getPrintName());
            antiPatternDetector.setSqlQueries(loadSqlFile(antiPatternDetector.getSqlFileNames()));
        }

        LOGGER.info("-------FINISHED READING SQL FROM FILES-------");
    }


    /**
     * Method for loading list of sql files from given list of files
     *
     * @param fileNames list of files with sql queries
     * @return list of queries
     */
    private List<String> loadSqlFile(List<String> fileNames) {
        List<String> queries = new ArrayList<>();

        // walk through all sql filenames and load all sql queries
        for (String fileName : fileNames) {

            LOGGER.info("Reading sql query from file " + fileName);

            try {
                URL url = getClass().getClassLoader().getResource(QUERY_DIR + fileName);
                BufferedReader read = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String line;
                while ((line = read.readLine()) != null) {
                    if (line.startsWith("select") || line.startsWith("set") && line.charAt(line.length() - 1) == ';') {
                        queries.add(line);
                    }
                }
                read.close();
            } catch (IOException e) {
                LOGGER.error("Cannot read sql from file " + fileName);
                return queries;
            }
        }
        return queries;
    }


    /**
     * Method for reading anti-pattern parameters from json file
     *
     * @param jsonFileName Name of the file
     * @return AntiPattern object
     */
    public AntiPattern getAntiPatternFromJsonFile(String jsonFileName){
        String json = "";  // json configuration file content as string

        LOGGER.info("Reading anti-pattern from json file " + jsonFileName);

        try {
            URL url = getClass().getClassLoader().getResource(AP_DIR + jsonFileName);

            BufferedReader read = new BufferedReader(
                    new InputStreamReader(url.openStream()));

            String line;
            while ((line = read.readLine()) != null) {
                json += line;
            }
        } catch (Exception e) {
            LOGGER.warn("Cannot read anti-pattern from json file " + jsonFileName);
            return null;
        }

        return parseAntiPatternFromJson(json);
    }

    /**
     * Method for parsing json file content and making AntiPattern object
     * @param jsonString Json file content
     * @return AntiPattern object made from json file
     */
    private AntiPattern parseAntiPatternFromJson(String jsonString){
        Long APid;
        JsonNode node = null;
        String APPrintName, APName, APDescription, APCatalogueFileName;

        // reading of anti-pattern basic information
        try {
            node = JsonParser.parse(jsonString);

            APid = Long.parseLong(node.get("id").asText());
            APPrintName = node.get("printName").asText();
            APName = node.get("name").asText();
            APDescription = node.get("description").asText();
            APCatalogueFileName = node.get("catalogueFileName") != null ? node.get("catalogueFileName").asText() : null; // optional field
        }
        catch(Exception e){
            LOGGER.error(e.getMessage());
            return null;
        }

        // reading of thresholds
        Map<String, Threshold> thresholdMap = new HashMap<>();
        JsonNode array = node.get("thresholds");
        Threshold tmpThreshold = null;

        String thresholdName, thresholdType, thresholdPrintName, thresholdDescription, thresholdErrorMess;

        for(int i = 0; i < array.size(); i++){
            JsonNode tmpNode = array.get(i);
            try {
                thresholdName = tmpNode.get("thresholdName").asText();
                thresholdType = tmpNode.get("thresholdType").asText();
                thresholdPrintName = tmpNode.get("thresholdPrintName").asText();
                thresholdDescription = tmpNode.get("thresholdDescription").asText();
                thresholdErrorMess = tmpNode.get("thresholdErrorMess").asText();
            }
            catch(Exception e){
                LOGGER.error(e.getMessage());
                return null;
            }

            // creating of Threshold object and storing it in map
            tmpThreshold = getThreshold(thresholdType, thresholdName, thresholdPrintName, thresholdDescription, thresholdErrorMess);
            thresholdMap.put(thresholdName, tmpThreshold);
        }

        // creating of the AntiPattern object
        AntiPattern result = new AntiPattern(APid, APPrintName, APName, APDescription, thresholdMap, APCatalogueFileName);

        return result;
    }

    /**
     * Get all loaded AP from global value.
     *
     * @return list of AP
     */
    public List<AntiPatternDetector> getAllAntiPatterns() {
        List<AntiPatternDetector> antiPatternDetectors = new ArrayList<>(this.antiPatternDetectors.values());
        Collections.sort(antiPatternDetectors, Comparator.comparing(AntiPatternDetector -> AntiPatternDetector.getAntiPatternModel().getName(), String.CASE_INSENSITIVE_ORDER));
        return antiPatternDetectors;
    }

    /**
     * Get AP by given ID.
     *
     * @param id AP ID
     * @return AP
     */
    public AntiPatternDetector getAntiPatternById(Long id) {
        return this.antiPatternDetectors.getOrDefault(id, null);
    }

    /**
     * Method for creating of the Threshold object from given parameters
     *
     * @param thresholdType Type of the threshold
     * @param name Threshold name
     * @param printName Threshold name for print
     * @param description Short description of the threshold
     * @param errorMessage Error message to display if threshold's value is set wrong
     * @return Threshold object
     */
    private Threshold getThreshold(String thresholdType, String name, String printName, String description, String errorMessage){

        if(thresholdType.equals("Percentage") || thresholdType.equals("PositiveFloat") || thresholdType.equals("PositiveInteger") || thresholdType.equals("String")){
            return new Threshold(name, printName, description, errorMessage, thresholdType);
        }

        return null;
    }

    public String getOperationalizationDirPathName(){
        try {
            return new FileSystemResource(OPERATIONALIZATION_DIR).getFile().getAbsolutePath();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public String getOperationalizationImgDirFileName(){
        try {
            return new FileSystemResource(OPERATIONALIZATION_IMG_DIR).getFile().getAbsolutePath();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
