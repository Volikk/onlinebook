package com.example.demo.repository.spec;

import com.example.demo.model.Book;
import com.example.demo.repository.book.SpecificationProvider;
import com.example.demo.repository.book.impl.BookSpecificationProviderKeys;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return BookSpecificationProviderKeys.TITLE;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, cb) -> root.get(BookSpecificationProviderKeys.TITLE)
                .in(Arrays.stream(params).toArray());
    }
}
