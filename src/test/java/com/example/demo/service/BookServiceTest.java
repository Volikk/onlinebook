package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.dto.BookDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.impl.BookServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void findById_ValidId_ShouldReturnBookDto() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Solar Power Basics");

        BookDto expectedDto = new BookDto();
        expectedDto.setId(bookId);
        expectedDto.setTitle("Solar Power Basics");

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectedDto);

        BookDto actualDto = bookService.findById(bookId);

        assertThat(actualDto).isNotNull();
        assertThat(actualDto.getTitle()).isEqualTo("Solar Power Basics");
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void findById_InvalidId_ShouldThrowEntityNotFoundException() {
        Long invalidId = 999L;
        when(bookRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.findById(invalidId))
                .isInstanceOf(EntityNotFoundException.class);

        verify(bookRepository, times(1)).findById(invalidId);
    }
}
