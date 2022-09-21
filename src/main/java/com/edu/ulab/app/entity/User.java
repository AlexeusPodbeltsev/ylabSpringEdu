package com.edu.ulab.app.entity;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
public class User extends BaseEntity {

    @Builder
    public User(Long id, String fullName, String title, int age) {
        super(id);
        this.fullName = fullName;
        this.title = title;
        this.age = age;
    }

    private String fullName;
    private String title;
    private int age;
}
