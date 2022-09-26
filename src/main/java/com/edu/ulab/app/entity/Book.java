package com.edu.ulab.app.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {
    private Long userId;
    private String title;
    private String author;
    private Long pageCount;

    @Builder
    public Book(Long id, Long userId, String title, String author, Long pageCount) {
        super(id);
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.pageCount = pageCount;
    }

}
