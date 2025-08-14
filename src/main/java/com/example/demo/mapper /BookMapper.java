package com.example.demo.mapper;

import com.example.demo.config.MapperConfig;
import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, componentModel = "spring")
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    @Mapping(target = "id", ignore = true) // не оновлюємо id
    void updateBookFromDto(CreateBookRequestDto dto, @MappingTarget Book book);
}
