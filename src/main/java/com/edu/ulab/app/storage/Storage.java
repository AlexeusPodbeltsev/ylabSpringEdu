package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.BaseEntity;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.exception.NullArgumentException;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Abstract class Storage represents a local memory of the app.
 *
 * @param <T> is entity class which extends BaseEntity
 * @param <ID> is id for this entity class
 */
@Slf4j
public abstract class Storage<T extends BaseEntity, ID extends Long> {
    /**
     * map is what app uses to do basic operations with data.
     */
    protected Map<Long, T> map = new HashMap<>();
    /**
     * nextId is value for entity's ids.
     */
    private Long nextId = 1L;

    /**
     * Return all objects in the Map.
     * @return ArrayList of all values in Map.
     */
    List<T> findAll() {
        return new ArrayList<>(map.values());
    }

    /**
     * Search entity object by its id.
     *
     * @param id entity's id.
     * @return found entity if it in the map.
     * @throws NotFoundException if map doesn't have key, which equals id,
     */
    T findById(ID id) {
        T object = map.get(id);
        if (object == null) {
            log.error("Object with id={} is not found", id);
            throw new NotFoundException("Object  with id: " + id + " not found");
        }
        return object;
    }

    /**
     * Save or Update entity object.
     * Firstly, the method check object id, if id == null,
     * then incremented id generated.
     * Secondly, if map doesn't have this id as a key, it would put a new key-value pair.
     * If the map is already have this one, then it would replace old value with the new one.
     * @param object is entity we want to save or update.
     * @return saved entity object.
     */
    T save(T object) {
        if (object != null) {
            if (object.getId() == null) {
                object.setId(getNextId());
                log.info("Increment id value");
            }
            map.put(object.getId(), object);
            log.info("Put id={} and object={} in to the map", object.getId(),object);
        } else {
            log.error("Given object is null");
            throw new NullArgumentException("Object cannot be null");
        }
        return object;
    }

    /**
     * Delete entity from the map by its id.
     *
     * @param id is using for searching right entity.
     * @throws NotFoundException if map doesn't have key, which equals id,
     */
    void deleteById(ID id) {
        T object = map.remove(id);
        log.info("Object with id={} has been removed from map", id);
        if (object == null) {
            log.error("Object with id= {} not found", id);
            throw new NotFoundException("Object with id: " + id + " not found");
        }
    }

    /**
     *Increment id.
     * @return id
     */
    private Long getNextId() {
        return nextId++;
    }
    //todo создать хранилище в котором будут содержаться данные
    // сделать абстракции через которые можно будет производить операции с хранилищем
    // продумать логику поиска и сохранения
    // продумать возможные ошибки
    // учесть, что при сохранеии юзера или книги, должен генерироваться идентификатор
    // продумать что у узера может быть много книг и нужно создать эту связь
    // так же учесть, что методы хранилища принимают друго тип данных - учесть это в абстракции
}
