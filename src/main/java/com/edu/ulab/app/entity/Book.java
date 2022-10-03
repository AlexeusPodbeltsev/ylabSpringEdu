package com.edu.ulab.app.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "book", schema = "ulab_edu")
public class Book extends BaseEntity {

    @Column(nullable = false)
    private String title;


    @Column(nullable = false)
    private String author;


    @Column(nullable = false)
    private Integer pageCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;

}
