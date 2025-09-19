package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookDtoWithoutCategoryIds;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.dto.UpdateBookRequestDto;
import com.example.demo.model.Book;
import com.example.demo.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    BookDto toDto(Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    void updateFromDto(@MappingTarget Book book, UpdateBookRequestDto requestDto);

    void updateModel(UpdateBookRequestDto updateBookRequestDto, @MappingTarget Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            Set<Long> ids = book.getCategories().stream()
                    .map(Category::getId)
                    .collect(Collectors.toSet());
            bookDto.setCategoryIds(ids);
        }
    }

    default Set<Category> mapCategoryIdsToCategories(Set<Long> categoryIds) {
        if (categoryIds == null) {
            return null;
        }
        return categoryIds.stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto dto) {
        book.setCategories(mapCategoryIdsToCategories(dto.getCategoryIds()));
    }

    @AfterMapping
    default void setCategories(@MappingTarget Book book, UpdateBookRequestDto dto) {
        if (dto.getCategoryIds() != null) {
            book.setCategories(mapCategoryIdsToCategories(dto.getCategoryIds()));
        }
    }
}
