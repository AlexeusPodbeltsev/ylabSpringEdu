package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserStorage extends Storage<User, Long> {


    @Override
    public User findById(Long id) {
        return super.findById(id);
    }

    @Override
    public User save(User object) {
        return super.save(object);
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
