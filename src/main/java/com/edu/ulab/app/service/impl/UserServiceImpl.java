package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.exception.NullArgumentException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl class is implement basic CRUD operations for user.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    public UserServiceImpl(UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    /**
     * Method which saves new users in storage.
     * Firstly, it maps userDto to userEntity.
     * Then save new user.
     * And finally maps userEntity to userDto.
     * @param userDto object received from upper layer.
     * @return saved user mapped to userDto.
     * @throws NullArgumentException if given userDto is null.
     */
    @Override
    public UserDto createUser(UserDto userDto) {
        if (userDto == null) {
            log.error("Given userDto is null");
            throw new NullArgumentException("UserDto cannot be null");
        }
        log.info("Got userDto {} in createUser method", userDto);
        User user = userMapper.userDtoToUserEntity(userDto);
        log.info("Mapped to userEntity {}", user);
        user = userStorage.save(user);
        log.info("Saved user {}", user);
        userDto = userMapper.userEntityToUserDto(user);
        log.info("Mapped userEntity to userDto {}", userDto);
        return userDto;
    }

    /**
     * Method which updates users in storage.
     * Firstly, it maps userDto to userEntity.
     * Then update user data.
     * And finally maps userEntity to userDto.
     * @param userDto object received from upper layer.
     * @return updated user mapped to userDto.
     * @throws NullArgumentException if given userDto is null.
     */
    @Override
    public UserDto updateUser(UserDto userDto) {
        if (userDto == null) {
            log.error("Given userDto is null");
            throw new NullArgumentException("UserDto cannot be null");
        }
        log.info("Got userDto {} in updateUser method", userDto);
        User user = userMapper.userDtoToUserEntity(userDto);
        log.info("Mapped to userEntity {}", user);
        user = userStorage.save(user);
        log.info("Updated user {}", user);
        userDto = userMapper.userEntityToUserDto(user);
        log.info("Mapped userEntity to userDto {}", userDto);
        return userDto;
    }

    /**
     * Getting User from storage by his id.
     * @param id is user's id.
     * @return user from storage mapped to userDto.
     */
    @Override
    public UserDto getUserById(Long id) {
        log.info("Got book id {} in getBookById method", id);
        User user = userStorage.findById(id);
        log.info("Found user {} by id={}", user, id);
        return userMapper.userEntityToUserDto(user);
    }

    /**
     * Delete user from storage using his id.
     * @param id is user's id.
     */
    @Override
    public void deleteUserById(Long id) {
        log.info("Got user id {} in deleteUserById method", id);
        userStorage.deleteById(id);
        log.info("User with id={} has been deleted", id);
    }
}
