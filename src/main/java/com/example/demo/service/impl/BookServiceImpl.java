package com.example.demo.service.impl;

import com.example.demo.dto.BookDto;
import com.example.demo.dto.CreateBookRequestDto;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(CreateBookRequestDto requestBook) {
        Book book = bookMapper.toModel(requestBook);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public List<BookDto> getAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateBook(Long id, CreateBookRequestDto updateRequest) {
        Book existingBook = bookRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        existingBook.setTitle(updateRequest.getTitle());
        existingBook.setAuthor(updateRequest.getAuthor());
        existingBook.setIsbn(updateRequest.getIsbn());
        existingBook.setPrice(updateRequest.getPrice());
        existingBook.setDescription(updateRequest.getDescription());
        existingBook.setCoverImage(updateRequest.getCoverImage());

        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public void softDeleteBook(Long id) {
        Book book = bookRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        book.setDeleted(true);
        bookRepository.save(book);
    }
}
