package com.checkcheck.ecoreading.domain.images.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "images")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter @Setter
public class Images extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long image_id;

    @ManyToOne(fetch = FetchType.LAZY) //books:images = 1:n
    @JoinColumn(name = "book_id")
    private Books books;

    @Column(name = "image_url", nullable = false)
    private String image_url;


}
