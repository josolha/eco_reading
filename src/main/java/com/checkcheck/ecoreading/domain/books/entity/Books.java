package com.checkcheck.ecoreading.domain.books.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;
import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.delivery.entity.Delivery;
import com.checkcheck.ecoreading.domain.images.entity.Images;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import com.checkcheck.ecoreading.domain.images.entity.Images;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "books")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Books extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long book_id;

    @ManyToOne(fetch = FetchType.LAZY) // boards:books = 1:n
    @JoinColumn(name = "board_id")
    private Boards boardId;

    @OneToOne
    private Delivery delivery;

    @OneToOne(mappedBy = "bookId")
    private Transactions transactions;

    @Builder.Default
    @OneToMany(
            mappedBy = "books", // mappedBy는 주인이 아님을 명시
            cascade = CascadeType.ALL)
            /* cascade옵션은 부모 엔티티에 대한 동작을 자식 엔티티로 전파할 때 사용됨.
             모든 영속성 동작(PERSIST, MERGE, REMOVE, REFRESH, DETACH)을 자식 엔티티로 전파하겠다는 의미.
             즉 부모엔티티에 대한 변경사항이 자식 엔티티에도 적용된다. */
    private List<Images> imagesList = new ArrayList<>();  // Todo: 변수명 images-> imageList로 변경

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "pubdate")
    private String pubdate;

    @Column(name = "description")
    @Lob //lob은 clob 데이터베이스에서 VARCHAR보다 큰 데이터를 담고 싶을 때 사용한다.
    private String description;

    @Column(name = "grade")
    private String grade;

    // 연관관계 메서드
    // 이미지를 올리면 Books의 이미지리스트에 추가가 되면서 또 Images 엔티티에 추가가 된다.
    public void addImage(Images image) {
        imagesList.add(image);
        image.setBooks(this);
    }

    // 연관관계 메서드
    // 책 한 권을 올리면 Transactions 엔티티에 추가가 된다.
    public void addTransaction(Transactions transactions) {
        this.transactions = transactions;
        transactions.setBooks(this);
    }

}


