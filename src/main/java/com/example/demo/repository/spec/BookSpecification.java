package com.example.demo.repository.spec;

import com.example.demo.dto.BookSearchParametersDto;
import com.example.demo.model.Book;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecification {
    public static Specification<Book> withSearchParams(BookSearchParametersDto params) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (params.title() != null && !params.title().isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("title")), "%"
                                + params.title().toLowerCase() + "%"));
            }

            if (params.author() != null && !params.author().isBlank()) {
                predicate = cb.and(predicate,
                        cb.like(cb.lower(root.get("author")), "%"
                                + params.author().toLowerCase() + "%"));
            }

            if (params.isbn() != null && !params.isbn().isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(root.get("isbn"), params.isbn()));
            }

            return predicate;
        };
    }
}
