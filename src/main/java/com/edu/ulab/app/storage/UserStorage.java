package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Person;
import org.springframework.stereotype.Component;

/**
 * UserStorage class is a component, which extends abstract Storage class.
 * This class let us work with User persist operations
 */
@Component
public class UserStorage extends Storage<Person, Long> {


    @Override
    public Person findById(Long id) {
        return super.findById(id);
    }

    @Override
    public Person save(Person object) {
        return super.save(object);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
