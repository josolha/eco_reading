package com.checkcheck.ecoreading.domain.boards.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;

import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.images.entity.Images;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "boards")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Boards extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(
            mappedBy = "boards",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Books> booksList = new ArrayList<>();

    @OneToOne
    // 기부어 아이디
    private Users user_id;

    // 기부어의 한마디
    @Column(name = "message")
    private String message;

    // 연관관계 메서드
    // 책 한 권을 올리면 Boards에 Book 추가되면서 Books엔티티에도 추가
    public void addBook(Books book) {
        booksList.add(book);
        book.setBoards(this);
    }
   
}
