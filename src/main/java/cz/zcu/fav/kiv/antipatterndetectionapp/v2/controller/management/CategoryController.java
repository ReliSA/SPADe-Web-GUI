package cz.zcu.fav.kiv.antipatterndetectionapp.v2.controller.management;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Category;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.AdditionalInformationDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.CategoryChangeRequest;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.CategoryDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.GeneralResponseDto;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.service.management.category.ICategories;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * This class contains all endpoints connected to Categories
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("v2/management")
public class CategoryController {

    private final ICategories categories;

    @PostMapping("/getCategories")
    public ResponseEntity<GeneralResponseDto<CategoryDto, AdditionalInformationDto<String>>> getCategories(@RequestBody Map<String, String> requestData) {
        return categories.findCategoriesForProject(requestData.get("projectId"));
    }

    @PostMapping("/changeCategory")
    public ResponseEntity<GeneralResponseDto<CategoryDto, AdditionalInformationDto<String>>> changeCategory(@RequestBody CategoryChangeRequest categoryChangeRequest) {
        return categories.handleCategoryChangeRequest(categoryChangeRequest);
    }
}
