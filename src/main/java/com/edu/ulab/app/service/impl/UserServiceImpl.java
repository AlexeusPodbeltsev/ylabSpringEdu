package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.User;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.UserStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    public UserServiceImpl(UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userDto == null) {
            throw new RuntimeException("UserDto cannot be null");
        }
        User user = userMapper.userDtoToUserEntity(userDto);
        user = userStorage.save(user);
        userDto = userMapper.userEntityToUserDto(user);
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        if (userDto == null) {
            throw new RuntimeException("UserDto cannot be null");
        }
        User user = userMapper.userDtoToUserEntity(userDto);
        user = userStorage.save(user);
        userDto = userMapper.userEntityToUserDto(user);
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userStorage.findById(id);
        return userMapper.userEntityToUserDto(user);
    }

    @Override
    public void deleteUserById(Long id) {
        userStorage.deleteById(id);
    }
}
