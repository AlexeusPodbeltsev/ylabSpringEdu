package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.exception.NullArgumentException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BookServiceImpl class is implement basic CRUD operations for book using jpa.
 */
@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository,
                           BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    /**
     * Method which saves new books in database.
     * Firstly, it maps bookDto to book.
     * Then save new book.
     * And finally maps book to bookDto.
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
        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped book: {}", book);
        Book savedBook = bookRepository.save(book);
        log.info("Saved book: {}", savedBook);
        return bookMapper.bookToBookDto(savedBook);
    }

    /**
     * Method which update books in storage.
     * Firstly, it maps bookDto to book.
     * After that find book by its id.
     * Then set received values and save this book data in database.
     * And finally maps book to bookDto.
     * @param bookDto object received from upper layer.
     * @return updated book mapped to bookDto.
     * @throws NullArgumentException if given bookDto is null.
     * @throws NotFoundException if database doesn't contain received book
     */
    @Override
    public BookDto updateBook(BookDto bookDto) {
        if (bookDto == null) {
            log.error("Given bookDto is null");
            throw new NullArgumentException("BookDto cannot be null");
        }
        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped book: {}", book);

        Book foundBookToUpdate = bookRepository.findByIdForUpdate(book.getId())
                .orElseThrow(() -> new NotFoundException("Book with id=" + book.getId() + " not found"));
        log.info("Found book to update {}", foundBookToUpdate);
        book.setTitle(book.getTitle());
        book.setAuthor(book.getAuthor());
        book.setPageCount(book.getPageCount());

        Book updatedBook = bookRepository.save(foundBookToUpdate);
        log.info("Updated book {}", updatedBook);

        return bookMapper.bookToBookDto(updatedBook);
    }

    /**
     * Getting Book from database by its id.
     * @param id is book's id.
     * @return book from storage mapped to bookDto.
     * @throws NotFoundException if book with received not found
     */
    @Override
    public BookDto getBookById(Long id) {
        Book foundBook = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id=" + id + " not found"));
        log.info("Found book with id={}: {}", id, foundBook);
        BookDto bookDto = bookMapper.bookToBookDto(foundBook);
        log.info("Mapped book to bookDto {}", bookDto);
        return bookDto;
    }

    /**
     * Delete book from storage using its id.
     * @param id is book's id.
     * @throws NotFoundException if book with received id not found
     */
    @Override
    public void deleteBookById(Long id) {
        Book bookToDelete = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id=" + id + " not found"));
        log.info("Book to delete {}", bookToDelete);
        bookRepository.deleteById(bookToDelete.getId());
        log.info("Book {} was deleted", bookToDelete);
    }

    /**
     * Find all books using user id
     * @param id user id
     * @return list of mapped book  to bookDtos where userId equals to arg's id
     * @throws NotFoundException if there are no books with received user id in database
     */
    @Override
    public List<BookDto> findBooksByUserId(Long id) {
        List<Book> foundBookByUserId = bookRepository.findBooksByUserId(id);
        if (foundBookByUserId == null) {
            throw new NotFoundException("There are no books in database with user id=" + id);
        }
        log.info("Found list of books in database with user id={}, {} ", id, foundBookByUserId);
        List<BookDto> bookDtoList = foundBookByUserId.stream().map(bookMapper::bookToBookDto).toList();
        log.info("List of books mapped to list of bookDtos {}", bookDtoList);
        return bookDtoList;
    }
}
