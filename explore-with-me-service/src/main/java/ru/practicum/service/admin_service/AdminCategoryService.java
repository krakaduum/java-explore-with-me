package ru.practicum.service.admin_service;

import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;

public interface AdminCategoryService {
    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(long catId);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);
}
