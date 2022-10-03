package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    BookMapper bookMapper;

    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void saveBook_Test() {
        //given
        Person person = new Person();
        person.setId(1L);

        BookDto bookDto = createBookDto(1L, "test author", "test title", 1000);
        BookDto result = createBookDto(1L, 1L, "test author", "test title", 1000);

        Book book = createBook("test title", "test author", 1000, person);
        Book savedBook = createBook(1L, "test title", "test author", 1000, person);

        //when

        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);


        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
    }


    @Test
    @DisplayName("Обновление данных книги. Должно пройти успешно.")
    void updateBook_Test() {
        //given
        Person person = new Person();
        person.setId(1L);

        BookDto bookDto = createBookDto(1L, 1L, "updated test author", "updated test title", 2000);

        Book mappedBook = createBook(1L, "updated test title", "updated test author", 2000, person);

        Book foundByIdToUpdateBook = createBook(1L, "test title", "test author", 1000, person);

        Book updatedBook = createBook(1L, "updated test title", "updated test author", 2000, person);

        BookDto result = createBookDto(1L, 1L, "updated test author", "updated test title", 2000);

        //when
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(mappedBook);
        when(bookRepository.findByIdForUpdate(mappedBook.getId())).thenReturn(Optional.of(foundByIdToUpdateBook));
        when(bookRepository.save(foundByIdToUpdateBook)).thenReturn(updatedBook);
        when(bookMapper.bookToBookDto(updatedBook)).thenReturn(result);


        //then
        BookDto bookDtoResult = bookService.updateBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
        assertEquals("updated test author", bookDtoResult.getAuthor());
        assertEquals("updated test title", bookDtoResult.getTitle());
        assertEquals(2000, bookDtoResult.getPageCount());
    }

    @Test
    @DisplayName("Поиск книги по ее id. Должно пройти успешно.")
    void getBookById_Test() {
        //given
        Person person = new Person();
        person.setId(1L);

        Long bookId = 2L;
        Book foundBook = createBook(bookId, "test title", "test author", 500, person);
        BookDto result = createBookDto(bookId, foundBook.getPerson().getId(), "test author", "test title", 500);

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(foundBook));
        when(bookMapper.bookToBookDto(foundBook)).thenReturn(result);

        BookDto bookDtoResult = bookService.getBookById(bookId);

        assertEquals(bookId, bookDtoResult.getId());
        assertEquals(500, bookDtoResult.getPageCount());

    }

    @Test
    @DisplayName("Удалить книгу по ее id. Должно пройти успешно.")
    void deleteBookById_Test() {
        //given
        Person person = new Person();
        person.setId(1L);

        Long bookId = 2L;
        Book bookToDelete = createBook(bookId, "test title", "test author", 400, person);

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookToDelete));

        //then
        bookService.deleteBookById(bookId);
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("Поиск книги по id пользовтеля. Должно пройти успешно.")
    void findBooksByUserId_Test() {
        //given
        Long personId = 1L;
        Person person = new Person();
        person.setId(personId);

        List<Book> foundBooksByUserId = new ArrayList<>();
        Book book1 = createBook(1L, "test title", "test author", 500, person);
        Book book2 = createBook(2L, "test title 2", "test author 2", 1000, person);
        foundBooksByUserId.add(book1);
        foundBooksByUserId.add(book2);

        BookDto bookDto1 = createBookDto(1L, personId, "test author", "test title", 500);
        BookDto bookDto2 = createBookDto(2L, personId, "test author 2", "test title 2", 1000);

        //when
        when(bookRepository.findBooksByPerson_Id(personId)).thenReturn(foundBooksByUserId);
        when(bookMapper.bookToBookDto(book1)).thenReturn(bookDto1);
        when(bookMapper.bookToBookDto(book2)).thenReturn(bookDto2);

        //then

        List<BookDto> bookDtoList = bookService.findBooksByUserId(personId);
        assertEquals(2, bookDtoList.size());
        for (BookDto bookDto : bookDtoList) {
            assertEquals(1L, bookDto.getUserId());
        }


    }



    @NotNull
    private BookDto createBookDto(Long id, Long userId, String author, String title, int pageCount) {
        BookDto bookDto = new BookDto();
        bookDto.setId(id);
        bookDto.setUserId(userId);
        bookDto.setAuthor(author);
        bookDto.setTitle(title);
        bookDto.setPageCount(pageCount);
        return bookDto;
    }

    @NotNull
    private BookDto createBookDto(Long userId, String author, String title, int pageCount) {
        BookDto bookDto = new BookDto();
        bookDto.setUserId(userId);
        bookDto.setAuthor(author);
        bookDto.setTitle(title);
        bookDto.setPageCount(pageCount);
        return bookDto;
    }

    @NotNull
    private Book createBook(Long id, String title, String author, int pageCount, Person person) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setPageCount(pageCount);
        book.setPerson(person);
        return book;
    }

    @NotNull
    private Book createBook(String title, String author, int pageCount, Person person) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPageCount(pageCount);
        book.setPerson(person);
        return book;
    }

}
