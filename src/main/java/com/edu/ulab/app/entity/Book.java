package com.edu.ulab.app.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class Book extends BaseEntity {

    @Builder
    public Book(Long id, Long userId, String title, String author, Long pageCount) {
        super(id);
        this.userId = userId;
        this.title = title;
        this.author = author;
        this.pageCount = pageCount;
    }

    private Long userId;
    private String title;
    private String author;
    private Long pageCount;
}
