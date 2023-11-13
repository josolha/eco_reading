package com.checkcheck.ecoreading.domain.boards.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;

import com.checkcheck.ecoreading.domain.boards.dto.InsertBoardDTO;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.delivery.entity.Delivery;
import com.checkcheck.ecoreading.domain.images.entity.Images;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "boards")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Boards extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Builder.Default
    @OneToMany(
            mappedBy = "boards",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Books> booksList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) //users: boards = 1:n
    @JoinColumn(name = "users_id")
    // 기부어 아이디
    private Users users;

    // DELIVERY 엔티티와 연결.
    @OneToOne(
            mappedBy = "boards",
            cascade = CascadeType.ALL
    )
    private Delivery delivery;

    // 기부어의 한마디
    private String message;


    // 연관관계 메서드
    // 책 한 권을 올리면 Boards에 Book 추가되면서 Books엔티티에도 추가
    public void addBook(Books book) {
        book.setBoards(this);
        this.getBooksList().add(book);
    }

    // board를 올리면 delivery에도 추가하겠다...
    public void addDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setBoards(this);
    }
    public void setUsers(Users users){
        this.users = users;
    }

    public void setMessage(String message) {this.message = message;}

}
