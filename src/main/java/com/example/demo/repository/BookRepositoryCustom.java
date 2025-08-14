package com.example.demo.repository;

import com.example.demo.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepositoryCustom {
    Book createBook(Book book);

    List<Book> getAll();

    Optional<Book> getBookById(Long id);
}
