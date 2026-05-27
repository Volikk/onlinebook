package com.example.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.demo.model.Book;
import com.example.demo.model.Category;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void saveAndFindById_ShouldWorkCorrectly() {
        Category category = new Category();
        category.setName("Green Energy");
        Category savedCategory = categoryRepository.save(category);

        Book book = new Book();
        book.setTitle("Solar Power Basics");
        book.setAuthor("Elon");
        book.setIsbn("978-3-16-148410-0");
        book.setPrice(new BigDecimal("29.99"));
        book.setCategories(Set.of(savedCategory));

        Book savedBook = bookRepository.save(book);
        Optional<Book> foundBook = bookRepository.findById(savedBook.getId());

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("Solar Power Basics");
        assertThat(foundBook.get().getCategories()).contains(savedCategory);
    }
}
