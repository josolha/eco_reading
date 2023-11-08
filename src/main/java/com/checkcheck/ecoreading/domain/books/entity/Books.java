package com.checkcheck.ecoreading.domain.books.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;
import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.images.entity.Images;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "books")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Books extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long book_id;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Boards boardId;

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

    @OneToOne(mappedBy = "book") // "bookId"는 Transactions 엔티티의 필드 이름
    private Transactions transactions;

    @OneToMany(mappedBy = "books")
    private List<Images> images;
}
