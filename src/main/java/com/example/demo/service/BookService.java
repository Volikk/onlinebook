package com.example.demo.service;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.BookSearchParametersDto;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.dto.UpdateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto update(Long id, UpdateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void delete(Long id);

    List<BookDto> searchBooks(BookSearchParametersDto params);
}
