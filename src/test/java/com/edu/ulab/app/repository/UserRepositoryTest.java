package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты репозитория {@link UserRepository}.
 */
@SystemJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить юзера. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPerson_thenAssertDmlCount() {
        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");
        person.setStatus("ACTIVE");

        //When
        Person result = userRepository.save(person);

        //Then
        assertThat(result.getAge()).isEqualTo(111);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }


    @DisplayName("Обновить пользователя. Число select должно ровняться 1")
    @Test
    @Rollback
    @Sql({
            "classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updatePerson_thenAssertDmlCount(){
        //given
         Person personToUpdate = userRepository.findByIdForUpdate(1001).get();
         personToUpdate.setFullName("updated full name");

         //when
         Person updatedPerson = userRepository.save(personToUpdate);

         //then
        assertThat(updatedPerson.getFullName()).isEqualTo("updated full name");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }


    @DisplayName("Поиск пользователя по id. Число select должно ровняться 1")
    @Test
    @Rollback
    @Sql({
            "classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getPersonById_thenAssertDmlCount(){
        //given
        Long personId = 1001L;

        //when
        Optional<Person> person = userRepository.findById(personId);

        //then
        assertThat(person).isNotNull();
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);

    }

    @DisplayName("Удаление пользователя по id. Число select должно ровняться 1")
    @Test
    @Rollback
    @Sql({
            "classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deletePersonById_thenAssertDmlCount(){
        //given
        Long personId = 1001L;

        //when
        userRepository.deleteById(personId);
        Optional<Person> person = userRepository.findById(personId);

        //then
        assertThat(person).isEmpty();
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);

    }

    // * failed
}
