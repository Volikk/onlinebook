package com.example.demo.repository.book.impl;

import com.example.demo.dto.BookSearchParametersDto;
import com.example.demo.model.Book;
import com.example.demo.repository.book.SpecificationBuilder;
import com.example.demo.repository.book.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    public static final String AUTHOR_KEY = "authors";
    public static final String TITLE_KEY = "titles";

    private final SpecificationProviderManager<Book> bookSpecificationProvider;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParameters) {
        Specification<Book> spec = Specification.where(null);
        spec = addSpecification(spec, TITLE_KEY, searchParameters.titles());
        spec = addSpecification(spec, AUTHOR_KEY, searchParameters.authors());
        return spec;
    }

    private Specification<Book> addSpecification(
            Specification<Book> spec,
            String key,
            String[] values
    ) {
        if (values != null && values.length > 0) {
            return spec.and(bookSpecificationProvider
                    .getSpecificationProvider(key)
                    .getSpecification(values));
        }
        return spec;
    }
}

