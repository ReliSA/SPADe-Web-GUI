package cz.zcu.fav.kiv.antipatterndetectionapp.service.managment;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Category;
import cz.zcu.fav.kiv.antipatterndetectionapp.repository.managment.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        Collections.sort(categories, Comparator.comparing(Category::getName, String.CASE_INSENSITIVE_ORDER));
        return categories;
    }

    @Override
    public Category getCategoryById(Long id) {
        Optional<Category> result = categoryRepository.findById(id);
        if (result.isEmpty()) {
            return null;
        } else {
            return result.get();
        }
    }

    @Override
    public void deleteCategories(List<Category> categories) {
        categoryRepository.deleteAll(categories);
    }
}
