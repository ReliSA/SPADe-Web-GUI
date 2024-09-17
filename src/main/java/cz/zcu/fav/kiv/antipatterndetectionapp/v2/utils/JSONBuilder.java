package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils;
import com.google.gson.Gson;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.AntiPatternDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ConfigurationDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.ThresholdDto;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Version 1.0 - only simple json can be built
 * @version 1.0
 * @author Vaclav Hrabik, Jiri Trefil
 */
public class JSONBuilder {
    /**
     * Method transforms map into string representation of JSON object
     * @param json key value pair that will be generated as json line
     * @return String representation of JSON object
     * */

    public static String buildJSON(Map<String,Object> json){

        JSONObject jsonObject = new JSONObject();
        for(String key : json.keySet()){
            jsonObject.put(key,parseJSONValue(json.get(key)));
        }
        String jsonString = jsonObject.toJSONString();

        return jsonString;
    }

    private static Object parseJSONValue(Object value){

        if(value instanceof HashMap<?,?>){
            Map<String,Object> map = null;
            try{
                map = (HashMap<String,Object>) value;
            }
            catch (ClassCastException e){
                throw new RuntimeException("Provided object of HashMap is not <String,Object> typed!");
            }
            JSONObject jsonObject = new JSONObject();
            for(String key : map.keySet())
                jsonObject.put(key,parseJSONValue(map.get(key)));
            return jsonObject;


        }
        if (value instanceof ArrayList){
            JSONArray jsonArray = new JSONArray();
            ArrayList<Object> list;
            list = (ArrayList<Object>) value;
            for(int i = 0,n=list.size(); i < n; i++)
                jsonArray.add(parseJSONValue(list.get(i).toString()));
            return jsonArray;
        }


        //some simple data type without any indentation, we can just return it
        return value;
    }

    /**
     * Transform string json to json object
     * @param jsonString json object in string type
     * @return JSONObject parsed string
     */
    public static JSONObject parseJSON(String jsonString) throws ParseException {
        if(jsonString == null||jsonString.length()==0) return null;
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
        return json;
    }

    public static Map<String, Map<String, String>> createMapFromString(String jsonContent){

        ConfigurationDto cfgDto = new Gson().fromJson(jsonContent, ConfigurationDto.class);

        Map<String, Map<String, String>> newAntiPatternMap = new HashMap<>();

        for (AntiPatternDto antiPatternDto : cfgDto.getConfiguration()) {

            String antiPatternName = antiPatternDto.getAntiPattern();

            Map<String, String> newThresholds = new HashMap<String, String>();

            for (ThresholdDto thresholdDto : antiPatternDto.getThresholds()) {

                String thresholdName = thresholdDto.getThresholdName();
                String value = thresholdDto.getValue();

                newThresholds.put(thresholdName, value);
            }
            newAntiPatternMap.put(antiPatternName, newThresholds);

        }

        return newAntiPatternMap;
    }




}
