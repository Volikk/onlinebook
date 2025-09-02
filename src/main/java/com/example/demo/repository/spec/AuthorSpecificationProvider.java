package com.example.demo.repository.spec;

import com.example.demo.model.Book;
import com.example.demo.repository.book.SpecificationProvider;
import com.example.demo.repository.book.impl.BookSpecificationProviderKeys;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return BookSpecificationProviderKeys.AUTHOR;
    }

    @Override
    public Specification<Book> getSpecification(String[] param) {
        return (root, query, criteriaBuilder) ->
                root.get(BookSpecificationProviderKeys.AUTHOR)
                        .in(Arrays.stream(param).toArray());
    }
}

