package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Slf4j
@Service
public class UserServiceImplTemplate implements UserService {
    private final String INSERT_SQL = "INSERT INTO PERSON(FULL_NAME, TITLE, AGE) VALUES (?,?,?)";
    private final String UPDATE_SQL = "UPDATE PERSON SET FULL_NAME = ?, TITLE=?, AGE=? WHERE ID=?";
    private final String SELECT_SQL = "SELECT * FROM PERSON WHERE ID=?";
    private final String DELETE_SQL = "DELETE FROM PERSON WHERE ID=?";
    private final JdbcTemplate jdbcTemplate;

    public UserServiceImplTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDto createUser(UserDto userDto) {


        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    return ps;
                }, keyHolder);

        userDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return userDto;
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        jdbcTemplate.update(UPDATE_SQL, userDto.getFullName(), userDto.getTitle(), userDto.getAge(), userDto.getId());
        log.info("User: {} was updated", userDto);
        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        UserDto userDto = jdbcTemplate.queryForObject(SELECT_SQL, new Object[]{id}, new UserDtoMapper());
        if (userDto == null){
            throw new NotFoundException("User with id=" + id + " not found");
        }
        log.info("Found user by id={}, {}", id, userDto);
        return userDto;
    }

    @Override
    public void deleteUserById(Long id) {
        jdbcTemplate.update(DELETE_SQL, id);
    }

    private static class UserDtoMapper implements RowMapper<UserDto> {
        public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserDto userDto = new UserDto();
            userDto.setId(rs.getLong("ID"));
            userDto.setFullName(rs.getString("FULL_NAME"));
            userDto.setTitle(rs.getString("TITLE"));
            userDto.setAge(rs.getInt("AGE"));
            return userDto;
        }
    }
}
