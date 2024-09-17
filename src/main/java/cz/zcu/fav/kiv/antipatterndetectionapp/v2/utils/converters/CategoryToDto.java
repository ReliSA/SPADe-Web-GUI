package cz.zcu.fav.kiv.antipatterndetectionapp.v2.utils.converters;

import cz.zcu.fav.kiv.antipatterndetectionapp.model.management.Category;
import cz.zcu.fav.kiv.antipatterndetectionapp.v2.model.CategoryDto;

import java.util.ArrayList;
import java.util.List;

public class CategoryToDto implements ClassToDto <Category, CategoryDto> {
    @Override
    public CategoryDto convert(Category source) {
        return CategoryDto
                .builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .build();
    }

    @Override
    public List<CategoryDto> convert(List<Category> source) {
        List<CategoryDto> categories = new ArrayList<>();
        source.stream().forEach(e -> categories.add(convert(e)));
        return categories;
    }
}
