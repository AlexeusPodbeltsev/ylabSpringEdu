package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NullArgumentException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.BookStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * BookServiceLocalStorageImpl class is implement basic CRUD operations for book using map as storage.
 */
@Slf4j
@Service
public class BookServiceLocalStorageImpl implements BookService {
    private final BookStorage bookStorage;
    private final BookMapper bookMapper;

    public BookServiceLocalStorageImpl(BookStorage bookStorage, BookMapper bookMapper) {
        this.bookStorage = bookStorage;
        this.bookMapper = bookMapper;
    }

    /**
     * Method which saves new books in storage.
     * Firstly, it maps bookDto to bookEntity.
     * Then save new book.
     * And finally maps bookEntity to bookDto.
     * @param bookDto object received from upper layer.
     * @return saved book mapped to bookDto.
     * @throws NullArgumentException if given bookDto is null.
     */
    @Override
    public BookDto createBook(BookDto bookDto) {
        if (bookDto == null) {
            log.error("Given bookDto is null");
            throw new NullArgumentException("BookDto cannot be null");
        }
        log.info("Got bookDto {} in createBook method", bookDto);
        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped to bookEntity {}", book);
        book = bookStorage.save(book);
        log.info("Saved book {}", book);
        bookDto = bookMapper.bookToBookDto(book);
        log.info("Mapped bookEntity to bookDto {}", bookDto);
        return bookDto;
    }
    /**
     * Method which update books in storage.
     * Firstly, it maps bookDto to bookEntity.
     * Then update new book.
     * And finally maps bookEntity to bookDto.
     * @param bookDto object received from upper layer.
     * @return updated book mapped to bookDto.
     * @throws NullArgumentException if given bookDto is null.
     */
    @Override
    public BookDto updateBook(BookDto bookDto) {
        if (bookDto == null) {
            log.error("Given bookDto is null");
            throw new NullArgumentException("BookDto cannot be null");
        }
        log.info("Got bookDto {} in updateBook method", bookDto);
        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped to bookEntity {}", book);
        Book updatedBook = bookStorage.save(book);
        log.info("Updated book {}", updatedBook);
        bookDto = bookMapper.bookToBookDto(updatedBook);
        log.info("Mapped bookEntity to bookDto {}", bookDto);
        return bookDto;
    }

    /**
     * Getting Book from storage by its id.
     * @param id is book's id.
     * @return book from storage mapped to bookDto.
     */
    @Override
    public BookDto getBookById(Long id) {
        log.info("Got book id {} in getBookById method", id);
        Book book = bookStorage.findById(id);
        log.info("Found book {} by id={}", book, id);
        return bookMapper.bookToBookDto(book);
    }

    /**
     * Delete book from storage using its id.
     * @param id is book's id.
     */
    @Override
    public void deleteBookById(Long id) {
        log.info("Got book id {} in deleteBookById method", id);
        bookStorage.deleteById(id);
        log.info("Book with id={} has been deleted", id);
    }

    /**
     * Find all books using user id
     * @param id user id
     * @return list of mapped book entities to bookDtos where userId equals to arg's id
     */
    @Override
    public List<BookDto> findBooksByUserId(Long id) {
        log.info("Got book id {} in findBooksByUserId method", id);
        List<Book> books = bookStorage.findAllByUserId(id);
        log.info("Found books by userID {}", books);
        return books.stream().map(bookMapper::bookToBookDto).collect(Collectors.toList());
    }
}
