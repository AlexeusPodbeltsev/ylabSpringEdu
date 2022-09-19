package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Book;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookStorage extends Storage<Book, Long> {
    @Override
    public Book findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Book save(Book object) {
        return super.save(object);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }

    public List<Book> findAllByUserId(Long id){
        List<Book> allBooks = findAll();
        List<Book> userBooks = allBooks.stream()
                .filter(book -> book.getUserId().equals(id))
                .collect(Collectors.toList());
        return userBooks;
    }

    @Override
    List<Book> findAll() {
        return super.findAll();
    }
}
