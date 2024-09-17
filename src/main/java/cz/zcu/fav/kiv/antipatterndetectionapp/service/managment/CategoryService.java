package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Category;

import java.util.List;

public interface CategoryService {

    /**
     * Method getting all categories from database
     * @return  List of all categories in database
     */
    List<Category> getAllCategories();

    /**
     * Method getting category by ID
     * @param id    ID of category
     * @return      Category with that ID
     */
    Category getCategoryById(Long id);

    /**
     * Method for deleting categories from database
     * @param categories    List of categories for deletion
     */
    void deleteCategories(List<Category> categories);
}
