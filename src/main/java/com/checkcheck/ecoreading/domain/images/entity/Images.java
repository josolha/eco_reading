package com.checkcheck.ecoreading.domain.images.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "images")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Builder
public class Images extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imagesId;

    @ManyToOne(fetch = FetchType.LAZY) //books:images = 1:n
    @JoinColumn(name = "books_id")
    private Books books;

    @Column(name = "images_url", nullable = false)
    private String imagesUrl;

    public void setBooks(Books book){
        this.books = book;
    }

}
