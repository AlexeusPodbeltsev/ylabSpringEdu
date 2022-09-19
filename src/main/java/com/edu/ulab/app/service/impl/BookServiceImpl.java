package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.BookStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookServiceImpl implements BookService {
    private final BookStorage bookStorage;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookStorage bookStorage, BookMapper bookMapper) {
        this.bookStorage = bookStorage;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(BookDto bookDto) {
        if (bookDto == null) {
            throw new RuntimeException("Book cannot be null");
        }
        Book book = bookMapper.bookDtoToBookEntity(bookDto);
        book = bookStorage.save(book);
        bookDto = bookMapper.bookEntityToBookDto(book);
        return bookDto;
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        if (bookDto == null) {
            throw new RuntimeException("Book cannot be null");
        }
        Book book = bookMapper.bookDtoToBookEntity(bookDto);
        bookDto = bookMapper.bookEntityToBookDto(bookStorage.save(book));
        return bookDto;
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookStorage.findById(id);
        return bookMapper.bookEntityToBookDto(book);
    }

    @Override
    public void deleteBookById(Long id) {
        bookStorage.deleteById(id);
    }

    @Override
    public List<BookDto> findBooksByUserId(Long id) {
        List<Book> books = bookStorage.findAllByUserId(id);
        return books.stream().map(bookMapper::bookEntityToBookDto).collect(Collectors.toList());
    }
}
