package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void savePerson_Test() {
        //given
        UserDto userDto = createUserDto("test name", "test title", "ACTIVE", 11);

        Person person = createPerson("test name", "test title", "ACTIVE", 11);

        Person savedPerson = createPerson(1L, "test name", "test title", "ACTIVE", 11);

        UserDto result = createUserDto(1L, "test name", "test title", "ACTIVE", 11);


        //when
        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);


        //then
        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(1L, userDtoResult.getId());
    }

    @Test
    @DisplayName("Изменение данных пользовтеля. Должно пройти успешно")
    void updatePerson_Test() {
        //given
        UserDto userDto = createUserDto(1L, "updated test name", "test title", "ACTIVE", 15);

        Person mappedPerson = createPerson(1L, "updated test name", "test title", "ACTIVE", 15);

        Person personFoundById = createPerson(1L, "test name", "test title", "ACTIVE", 11);

        Person updatedPerson = createPerson(1L, "updated test name", "test title", "ACTIVE", 15);

        UserDto result = createUserDto(1L, "updated test name", "test title", "ACTIVE", 15);

        //when
        when(userMapper.userDtoToPerson(userDto)).thenReturn(mappedPerson);
        when(userRepository.findByIdForUpdate(mappedPerson.getId())).thenReturn(Optional.of(personFoundById));
        when(userRepository.save(personFoundById)).thenReturn(updatedPerson);
        when(userMapper.personToUserDto(updatedPerson)).thenReturn(result);

        //then
        UserDto userDtoResult = userService.updateUser(userDto);
        assertEquals(1L, userDtoResult.getId());
        assertEquals(15, userDtoResult.getAge());
        assertEquals("updated test name", userDtoResult.getFullName());

    }

    @Test
    @DisplayName("Поиск пользователя по id. Должно пройти успешно")
    void getUserById_Test() {
        //given
        Long personId = 2L;
        Person foundPerson = createPerson(personId, "Test Person", "test", "ACTIVE", 14);

        UserDto result = createUserDto(personId, "Test Person", "test", "ACTIVE", 14);

        //when
        when(userRepository.findById(personId)).thenReturn(Optional.of(foundPerson));
        when(userMapper.personToUserDto(foundPerson)).thenReturn(result);

        //then
        UserDto userDtoResult = userService.getUserById(personId);
        assertEquals(personId, userDtoResult.getId());
        assertEquals(14, userDtoResult.getAge());
    }

    @Test
    @DisplayName("Удаление пользователя по его id. Должно пройти успешно")
    void deleteUserById() {
        //given
        Long personId = 2L;
        Person personToDelete = createPerson(personId, "test", "test", "ACTIVE", 11);

        //when
        when(userRepository.findById(personId)).thenReturn(Optional.of(personToDelete));
        //then
        userService.deleteUserById(personId);
        verify(userRepository, times(1)).deleteById(personId);
    }

    @DisplayName("Поиск пользователя по не существующему id.")
    @Test
    void findNotExistingBookById() {

        Long userId = 500L;

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User with id=" + userId + " not found");
    }


    private UserDto createUserDto(Long id, String fullName, String title, String status, int age) {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setFullName(fullName);
        userDto.setTitle(title);
        userDto.setStatus(status);
        userDto.setAge(age);
        return userDto;
    }

    private UserDto createUserDto(String fullName, String title, String status, int age) {
        UserDto userDto = new UserDto();
        userDto.setFullName(fullName);
        userDto.setTitle(title);
        userDto.setStatus(status);
        userDto.setAge(age);
        return userDto;
    }

    private Person createPerson(Long id, String fullName, String title, String status, int age) {
        Person person = new Person();
        person.setId(id);
        person.setFullName(fullName);
        person.setTitle(title);
        person.setStatus(status);
        person.setAge(age);
        return person;
    }

    private Person createPerson(String fullName, String title, String status, int age) {
        Person person = new Person();
        person.setFullName(fullName);
        person.setTitle(title);
        person.setStatus(status);
        person.setAge(age);
        return person;
    }
}
