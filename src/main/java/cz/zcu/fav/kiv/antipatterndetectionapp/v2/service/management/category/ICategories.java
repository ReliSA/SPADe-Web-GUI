package cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.management.category;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Category;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.AdditionalInformationDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.CategoryChangeRequest;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.CategoryDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.GeneralResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ICategories {

    /**
     * Returns the list of categories for given project id
     * @param projectId project ID
     * @return ResponseEntity with the list of categories
     */
    ResponseEntity<GeneralResponseDto<CategoryDto, AdditionalInformationDto<String>>> findCategoriesForProject(String projectId);

    /**
     * Manage the request for categories change according to the given data
     * @param categoryChangeRequest Input data with categories to change and type of the operation
     * @return ResponseEntity with the results of the operation
     */
    ResponseEntity<GeneralResponseDto<CategoryDto, AdditionalInformationDto<String>>> handleCategoryChangeRequest(CategoryChangeRequest categoryChangeRequest);

}
