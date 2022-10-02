package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NullArgumentException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * UserServiceLocalStorageImpl class is implement basic CRUD operations for user using map as a storage.
 */
@Slf4j
@Service
public class UserServiceLocalStorageImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    public UserServiceLocalStorageImpl(UserStorage userStorage, UserMapper userMapper) {
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
        Person person = userMapper.userDtoToPerson(userDto);
        log.info("Mapped to userEntity {}", person);
        person = userStorage.save(person);
        log.info("Saved user {}", person);
        userDto = userMapper.personToUserDto(person);
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
        Person person = userMapper.userDtoToPerson(userDto);
        log.info("Mapped to userEntity {}", person);
        person = userStorage.save(person);
        log.info("Updated user {}", person);
        userDto = userMapper.personToUserDto(person);
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
        Person person = userStorage.findById(id);
        log.info("Found user {} by id={}", person, id);
        return userMapper.personToUserDto(person);
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
