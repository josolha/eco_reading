package com.checkcheck.ecoreading.domain.books.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;
import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "books")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Books extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Boards boards;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "pubdate")
    private Date pubdate;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "grade")
    private String grade;

}
