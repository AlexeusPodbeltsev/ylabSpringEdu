package com.edu.ulab.app.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Getter
@Setter
@NoArgsConstructor
@Entity
public class Person extends BaseEntity {

    private String fullName;
    private String title;
    private int age;

    @Builder
    public Person(Long id, String fullName, String title, int age) {
        super(id);
        this.fullName = fullName;
        this.title = title;
        this.age = age;
    }


}
