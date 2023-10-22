package ru.practicum.service.admin_service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.exception.InvalidParamException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.admin_service.AdminCategoryService;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        var category = categoryRepository.findByName(newCategoryDto.getName());

        if (category.isPresent()) {
            throw new InvalidParamException("Категория с именем " + newCategoryDto.getName() + " уже существует");
        }

        return CategoryMapper.toCategoryDto(
                categoryRepository.save(CategoryMapper.toCategory(newCategoryDto)));
    }

    @Override
    public void deleteCategory(long catId) {
        Optional<Category> category = categoryRepository.findById(catId);

        if (category.isEmpty()) {
            throw new NoSuchElementException("Категории с идентификатором " + catId + " не существует");
        }

        if (!eventRepository.findAllByCategoryId(catId).isEmpty()) {
            throw new InvalidParamException("Невозможно удалить категорию с привязанными событиями");
        }

        categoryRepository.deleteById(catId);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Optional<Category> potentialDuplicateCategory = categoryRepository.findByName(categoryDto.getName());

        if (potentialDuplicateCategory.isPresent()
                && !catId.equals(potentialDuplicateCategory.get().getId())) {
            throw new InvalidParamException("Категория с именем " + categoryDto.getName() + " уже существует");
        }

        Optional<Category> category = categoryRepository.findById(catId);

        if (category.isEmpty()) {
            throw new InvalidParamException("Категории с идентификатором " + catId + " не существует");
        }

        category.get().setName(categoryDto.getName());

        return CategoryMapper.toCategoryDto(categoryRepository.save(category.get()));
    }
}
