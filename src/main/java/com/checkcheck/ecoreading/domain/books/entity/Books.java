package com.checkcheck.ecoreading.domain.books.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;
import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.delivery.entity.Delivery;
import com.checkcheck.ecoreading.domain.images.entity.Images;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import com.checkcheck.ecoreading.domain.images.entity.Images;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import com.checkcheck.ecoreading.domain.users.entity.Users;
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
@Builder
public class Books extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long booksId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // boards:books = 1:n
    @JoinColumn(name = "boards_id")
    private Boards boards;

    @OneToOne(mappedBy = "books",
            cascade = CascadeType.ALL)
    private Transactions transactions;

    @Builder.Default
    @OneToMany(
            mappedBy = "books", // mappedBy는 주인이 아님을 명시
            cascade = CascadeType.ALL)
            /* cascade옵션은 부모 엔티티에 대한 동작을 자식 엔티티로 전파할 때 사용됨.
             모든 영속성 동작(PERSIST, MERGE, REMOVE, REFRESH, DETACH)을 자식 엔티티로 전파하겠다는 의미.
             즉 부모엔티티에 대한 변경사항이 자식 엔티티에도 적용된다. */
    private List<Images> imagesList = new ArrayList<>();  // Todo: 변수명 images-> imageList로 변경

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String pubDate;
    @Lob //lob은 clob 데이터베이스에서 VARCHAR보다 큰 데이터를 담고 싶을 때 사용한다.
    private String description;
    private String grade;
    @Enumerated(EnumType.STRING)
    private BookProcessingMethod processing; //만약 등급이 똥휴지일때 처리하는 방법 정의 (true일 때 폐기처리, false일 때 착불반송)

    // 연관관계 메서드
    // 이미지를 올리면 Books의 이미지리스트에 추가가 되면서 또 Images 엔티티에 추가가 된다.
    public void addImages(Images image) {
        imagesList.add(image);
        image.setBooks(this);
    }

    // 연관관계 메서드
    // 책 한 권을 올리면 Transactions 엔티티에 추가가 된다.
    public void addTransactions(Transactions transactions) {
        this.transactions = transactions;
        transactions.setBooks(this);
    }
    public void setBoards(Boards boards){
        this.boards = boards;
    }
    public Books(String isbn, String title, String author, String publisher, String pubDate, String description) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.pubDate = pubDate;
        this.description = description;
        // 나머지 필드의 초기화는 생략 (필요에 따라 추가)
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGrade(String grade) {this.grade = grade;}
}


