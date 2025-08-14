package com.example.demo.repository;

import com.example.demo.model.Book;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {
    List<Book> findAllByDeletedFalse();

    Optional<Book> findByIdAndDeletedFalse(Long id);
}
