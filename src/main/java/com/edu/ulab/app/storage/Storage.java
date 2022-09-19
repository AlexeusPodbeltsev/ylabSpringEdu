package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.BaseEntity;
import com.edu.ulab.app.exception.NotFoundException;

import java.util.*;

public abstract class Storage<T extends BaseEntity, ID extends Long> {
    protected Map<Long, T> map = new HashMap<>();

    List<T> findAll() {
        return new ArrayList<>(map.values());
    }

    T findById(ID id) {
        T object = map.get(id);
        if (object == null) {
            throw new NotFoundException("Object  with id: " + id + " not found");
        }
        return object;
    }

    T save(T object) {
        if (object != null) {
            if (object.getId() == null) {
                object.setId(getNextId());
            }
            map.put(object.getId(), object);
        } else {
            throw new RuntimeException("Object cannot be null");
        }
        return object;
    }

    void deleteById(ID id) {
        T object = map.remove(id);
        if (object == null) {
            throw new NotFoundException("Object  with id: " + id + " not found");
        }
    }


    private Long getNextId() {
        Long nextId = null;
        try {
            nextId += 1;
        } catch (NoSuchElementException e) {
            nextId = 1L;
        }
        return nextId;
    }
    //todo создать хранилище в котором будут содержаться данные
    // сделать абстракции через которые можно будет производить операции с хранилищем
    // продумать логику поиска и сохранения
    // продумать возможные ошибки
    // учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор
    // продумать что у узера может быть много книг и нужно создать эту связь
    // так же учесть, что методы хранилища принимают друго тип данных - учесть это в абстракции
}
