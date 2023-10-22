package ru.practicum.service.public_service;

import ru.practicum.dto.CategoryDto;

import java.util.Collection;

public interface PublicCategoryService {
    Collection<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(long catId);
}
