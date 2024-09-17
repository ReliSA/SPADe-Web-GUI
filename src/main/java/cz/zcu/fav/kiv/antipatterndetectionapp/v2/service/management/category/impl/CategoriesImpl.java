package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.management.category.impl;

import cz.zcu.fav.kiv.antipatterndetectionapp.Constants;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.Project;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.interfaces.DatabaseObject;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.ProjectService;
import cz.zcu.fav.kiv.antipatterndetectionapp.service.managment.*;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.AdditionalInformationDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.CategoryChangeRequest;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.CategoryDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.GeneralResponseDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.activity.ActivityService;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.management.category.ICategories;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters.CategoryToDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoriesImpl implements ICategories {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
    private final ProjectService projectService;
    private final CategoryService categoryService;
    private final WorkUnitService workUnitService;
    private final IterationService iterationService;
    private final ActivityService activityService;
    private final PhaseService phaseService;
    private final String GENERAL_ERROR_MESSAGE = "ERROR: Application error occurred";
    private final CategoryToDto categoryMapper = new CategoryToDto();
    private final String PROJECT_NOT_FOUND_MESSAGE = "Project not found!";
    private final String CATEGORIES_NOT_FOUND_MESSAGE = "Categories not found for the project!";
    private final String INVALID_PROJECT_ID = "Provided project ID is in invalid format!";
    private final String CATEGORY_NOT_FOUND = "Category not found!";

    private Project getProjectById(Long id) {
        return projectService.getProjectById(id);
    }

    @Override
    public ResponseEntity<GeneralResponseDto<CategoryDto, AdditionalInformationDto<String>>> handleCategoryChangeRequest(CategoryChangeRequest categoryChangeRequest) {
        List<CategoryDto> categoryDtos = categoryChangeRequest.getCategories();
        Integer submitType = categoryChangeRequest.getSubmitType();

        // Checking input data
        if (categoryDtos != null && submitType != null) {
            if (submitType <= 0 || submitType > Constants.SUBMIT_TYPES) { // ERROR: Unknown submitType
                return createResponseEntity(HttpStatus.BAD_REQUEST, GENERAL_ERROR_MESSAGE, null, null);
            }
        }

        // Mapping CategoryDto to Category
        List<Category> categories = new ArrayList<>();
        for (CategoryDto categoryDto : categoryDtos) {
            Category categoryTmp = categoryService.getCategoryById(categoryDto.getId());
            if (categoryTmp != null) {
                categories.add(categoryTmp);
            }
            else {
                return createResponseEntity(HttpStatus.BAD_REQUEST, CATEGORY_NOT_FOUND, null, null);
            }
        }

        // Processing all selected categories
        Pair<String, Boolean> result = processSelectedCategories(submitType, categories);

        // Creating reponse information
        AdditionalInformationDto<String> additionalInformation = new AdditionalInformationDto<>();

        if (!result.getSecond()) {
            additionalInformation.addIntoCollection("successMessage", "All selected categories (" + categories.size() + ") were assign");
        } else {
            additionalInformation.addIntoCollection("informMessage", "Some categories cannot be assign, look at the log for more information");
        }
        additionalInformation.addIntoCollection("message", result.getFirst());

        return createResponseEntity(HttpStatus.OK, null, categoryMapper.convert(categories), additionalInformation);
    }

    private List<CategoryDto> getCategories(Project project) {

        if (project == null) {
            return null;
        }

        List<Category> categories = new ArrayList<>();
        for (ProjectInstance projectInstance : project.getProjectInstances()) {
            categories.addAll(projectInstance.getCategories());
        }

        List<CategoryDto> categoriesDto = categoryMapper.convert(categories);
        categoriesDto.sort(Comparator.comparing(CategoryDto::getName, String.CASE_INSENSITIVE_ORDER));

        return categoriesDto;
    }

    @Override
    public ResponseEntity<GeneralResponseDto<CategoryDto, AdditionalInformationDto<String>>> findCategoriesForProject(String projectId) {
        // --- The value from the request body is String, so there must be a validation if the string is number or not.
        final Long id = tryParse(projectId);
        if (id == Long.MIN_VALUE)
            return createResponseEntity(HttpStatus.BAD_REQUEST, INVALID_PROJECT_ID, null, null);

        // --- get project by ID
        final Project project = getProjectById(id);

        if (project == null)
            return createResponseEntity(HttpStatus.NOT_FOUND, PROJECT_NOT_FOUND_MESSAGE, null, null);

        // --- Get all categories for the specific project
        final List<CategoryDto> categories = getCategories(project);

        if (categories == null)
            return createResponseEntity(HttpStatus.NOT_FOUND, CATEGORIES_NOT_FOUND_MESSAGE, null, null);

        return createResponseEntity(HttpStatus.OK, null, categories, null);
    }

    private ResponseEntity<GeneralResponseDto<CategoryDto, AdditionalInformationDto<String>>> createResponseEntity(@NonNull HttpStatus status,
                                                                                                                   @Nullable String message,
                                                                                                                   @Nullable List<CategoryDto> categories,
                                                                                                                   @Nullable AdditionalInformationDto<String> additionalInfo) {
        return new ResponseEntity<>(
                GeneralResponseDto
                        .<CategoryDto, AdditionalInformationDto<String>>builder()
                        .message(message)
                        .responseBody(categories)
                        .additionalInformation(additionalInfo)
                        .build(), status);
    }

    private Long tryParse(String valueToParse) {
        try {
            return Long.parseLong(valueToParse);
        } catch (NumberFormatException e) {
            return Long.MIN_VALUE;
        }
    }

    private Pair<Integer, Long> extractTypeAndId(String submitId) {
        String[] parts = submitId.split(Constants.HTML_DELIMITER);
        try {
            Integer type = Integer.parseInt(parts[0]);
            Long id = Long.parseLong(parts[1]);
            return Pair.of(type, id);

        } catch (NumberFormatException e) {
            return null;
        }

    }

    private Pair<String, Boolean> processSelectedCategories(Integer submitType, List<Category> selectedCategories) {

        boolean errorOccurred = false;
        StringBuilder message = new StringBuilder();
        for (Category category : selectedCategories) {
            message.append("Category: ").append(category.getName()).append(System.getProperty("line.separator"));

            if (category.getWorkUnits().size() == 0) { //Inform user that there aren't any WorkUnits
                errorOccurred = true;
                message.append(Constants.INDENT)
                        .append("cannot be assigned due to non-existent work unit")
                        .append(System.getProperty("line.separator"))
                        .append(System.getProperty("line.separator"));
                continue;
            }

            DatabaseObject object = getNewObject(category.getWorkUnits(), submitType, category);

            for (WorkUnit workUnit : category.getWorkUnits()) {

                boolean hasAttr = true;
                switch (submitType) {
                    case 1:
                        if (workUnit.getIteration() == null) {
                            hasAttr = false;
                            workUnit.setIteration((Iteration) object);
                            workUnitService.saveWorkUnit(workUnit);
                        }
                        break;
                    case 2:
                        if (workUnit.getPhase() == null) {
                            hasAttr = false;
                            workUnit.setPhase((Phase) object);
                            workUnitService.saveWorkUnit(workUnit);
                        }
                        break;
                    case 3:
                        if (workUnit.getActivity() == null) {
                            hasAttr = false;
                            workUnit.setActivity((Activity) object);
                            workUnitService.saveWorkUnit(workUnit);
                        }
                        break;
                }

                if (!hasAttr) { // Transformation of selected category was done

                    message.append(Constants.INDENT)
                            .append("successfully assigned to WU with id ").append(workUnit.getId())
                            .append(System.getProperty("line.separator"));

                } else { // WorkUnit already has iteration, inform user

                    errorOccurred = true;
                    message.append(Constants.INDENT)
                            .append("cannot be assigned to WU with id ").append(workUnit.getId())
                            .append(" as it already exists with ").append(workUnit.getIteration().getName())
                            .append(System.getProperty("line.separator"));
                }
            }
            message.append(System.getProperty("line.separator"));
        }
        return Pair.of(message.toString(), errorOccurred);
    }

    private DatabaseObject getNewObject(Set<WorkUnit> workUnits, Integer submitType, Category category) {

        switch (submitType) {
            case 1:
                for (WorkUnit workUnit : workUnits) {
                    if (workUnit.getIteration() == null) {
                        Iteration iteration = new Iteration(category.getExternalId(),
                                category.getName(),
                                category.getDescription(),
                                category.getProjectInstance().getProjectId());
                        return iterationService.saveIteration(iteration);
                    }
                }
                break;
            case 2:
                for (WorkUnit workUnit : workUnits) {
                    if (workUnit.getPhase() == null) {
                        Phase phase = new Phase(category.getExternalId(),
                                category.getName(),
                                category.getDescription(),
                                category.getProjectInstance().getProjectId());
                        return phaseService.savePhase(phase);
                    }
                }
                break;
            case 3:
                for (WorkUnit workUnit : workUnits) {
                    if (workUnit.getActivity() == null) {
                        Activity activity = new Activity(category.getExternalId(),
                                category.getName(),
                                category.getDescription(),
                                category.getProjectInstance().getProjectId());
                        return activityService.saveActivity(activity);
                    }
                }
                break;
        }
        return null;
    }

}
