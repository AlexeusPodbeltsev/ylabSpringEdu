package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.exception.NullArgumentException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import com.edu.ulab.app.service.impl.UserServiceImplTemplate;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Component which provide an abstraction above Service layer
 */
@Slf4j
@Component
public class UserDataFacade {
    private final UserServiceImplTemplate userService;
    private final BookServiceImpl bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserServiceImplTemplate userService,
                          BookServiceImpl bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    /**
     * Create new user and books, and add them to the storage.
     * @param userBookRequest is User data and list of books for this user.
     * @return UserBookResponse which has user id and list of ids of his books.
     * @throws NullArgumentException if received List of Books is null.
     */
    @Transactional
    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);

        List<Long> bookIdList = Optional.ofNullable(userBookRequest.getBookRequests())
                .orElseThrow(() -> new NullArgumentException("List of books can't be null"))
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(createdUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }
    /**
     * Update user and books if they are in request.
     * @param userBookRequest is User data and list of books for this user.
     * @return UserBookResponse which has user id and ids of updated books.
     * @throws NullArgumentException if received List of Books is null.
     */
    @Transactional
    public UserBookResponse updateUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book update request {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);

        UserDto updatedUser = userService.updateUser(userDto);
        log.info("Updated user {}", updatedUser);

        List<Long> bookIdList = Optional.ofNullable(userBookRequest.getBookRequests())
                .orElseThrow(() -> new NullArgumentException("List of books can't be null"))
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(mappedBookDto -> mappedBookDto.setUserId(updatedUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book {}", mappedBookDto))
                .map(bookService::updateBook)
                .peek(updatedBook -> log.info("Updated book {}", updatedBook))
                .map(BookDto::getId)
                .toList();
        log.info("Collected updated book ids: {}", bookIdList);
        return UserBookResponse.builder()
                .userId(updatedUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    /**
     * Get user and books by userId.
     * @param userId is id of user we are looking for.
     * @return UserBookResponse which has user id and list of ids of his books.
     */
    @Transactional
    public UserBookResponse getUserWithBooks(Long userId) {
        log.info("Got user with id={} to find", userId);
        UserDto userDto = userService.getUserById(userId);
        log.info("Got userDto {} with user id={}", userDto, userId);
        List<Long> bookIdList = bookService.findBooksByUserId(userId)
                .stream()
                .filter(Objects::nonNull)
                .peek(bookDto -> log.info("Found bookDto {} with userId={}", bookDto, userId))
                .map(BookDto::getId)
                .collect(Collectors.toList());
        log.info("Collected found book ids: {}", bookIdList);
        return UserBookResponse.builder()
                .userId(userDto.getId())
                .booksIdList(bookIdList)
                .build();
    }
    /**
     * Delete user from storage and his books.
     * @param userId is id of user we want to delete.
     */
    @Transactional
    public void deleteUserWithBooks(Long userId) {
        log.info("Got user with id={} to delete", userId);
        bookService.findBooksByUserId(userId)
                .stream()
                .filter(Objects::nonNull)
                .peek(bookDto -> log.info("Found bookDto to delete {}", bookDto))
                .forEach(bookDto -> bookService.deleteBookById(bookDto.getId()));
        userService.deleteUserById(userId);
        log.info("User with id: {} has been deleted", userId);

    }
}
