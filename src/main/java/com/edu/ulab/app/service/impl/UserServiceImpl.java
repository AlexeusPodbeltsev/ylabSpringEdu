package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.exception.NullArgumentException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl class is implement basic CRUD operations for user using jpa.
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    /**
     * Method which saves new users in database.
     * Firstly, it maps userDto to person.
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
        Person user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);
        Person savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);
        return userMapper.personToUserDto(savedUser);
    }
    /**
     * Method which updates users in database.
     * Firstly, it maps userDto to person.
     * After that find user by his id.
     * Then set received values and save this user data in database.
     * And finally maps person to userDto.
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
        Person person = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", person);

        Person foundPersonToUpdate = userRepository.findByIdForUpdate(person.getId())
                .orElseThrow(() -> new NotFoundException("User with id=" + person.getId() + " not found"));
        log.info("Found user to update {}", foundPersonToUpdate);
        foundPersonToUpdate.setAge(person.getAge());
        foundPersonToUpdate.setFullName(person.getFullName());
        foundPersonToUpdate.setTitle(person.getTitle());

        Person updatedPerson = userRepository.save(foundPersonToUpdate);
        log.info("Updated user {}", updatedPerson);
        return userMapper.personToUserDto(updatedPerson);
    }

    /**
     * Getting User from database by his id.
     * @param id is user's id.
     * @return user from storage mapped to userDto.
     * @throws NotFoundException if user with given id not found
     */
    @Override
    public UserDto getUserById(Long id) {
        Person foundPerson = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id=" + id + " not found"));
        log.info("Found user with id={}: {}", id, foundPerson);
        UserDto userDto = userMapper.personToUserDto(foundPerson);
        log.info("Mapped user to userDto {}", userDto);
        return userDto;
    }

    /**
     * Delete user from storage using his id.
     * @param id is user's id.
     * @throws NotFoundException if user with given id not found
     */
    @Override
    public void deleteUserById(Long id) {
        Person personToDelete = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id=" + id + " not found"));
        log.info("User to delete {}", personToDelete);
        userRepository.deleteById(personToDelete.getId());
        log.info("User {} was deleted", personToDelete);
    }
}
