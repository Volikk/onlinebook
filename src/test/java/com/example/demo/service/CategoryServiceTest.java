package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dto.CategoryDto;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void saveCategory_ShouldReturnSavedCategoryDto() {
        CategoryDto requestDto = new CategoryDto();
        requestDto.setName("Green Energy");

        Category category = new Category();
        category.setName("Green Energy");

        Category savedCategory = new Category();
        savedCategory.setId(1L);
        savedCategory.setName("Green Energy");

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(requestDto);

        CategoryDto result = categoryService.save(requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Green Energy");
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
}
