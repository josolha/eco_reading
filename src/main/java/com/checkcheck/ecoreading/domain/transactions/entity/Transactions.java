package com.checkcheck.ecoreading.domain.transactions.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;
import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.delivery.entity.Delivery;
import com.checkcheck.ecoreading.domain.pointHistory.entity.PointHistory;
import lombok.*;
import javax.persistence.*;
import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Table(name = "transactions")
public class Transactions extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionsId;

    @Column(nullable = false)
    private Long giverId; //나눔글 올릴 시 giverID 넣기 필수
    private Long takerId;
    //todo: 기버아이디와 테이커 아이디 나눠서 Users 엔티티와 연결짓기 (되면)

    @Builder.Default
    @OneToMany(
            mappedBy = "transactions",
            cascade = CascadeType.ALL)
    private List<PointHistory> pointHistoryList = new ArrayList<>();

    @OneToOne(
            mappedBy = "transactions",
            cascade = CascadeType.ALL)
    private Delivery delivery;

    @OneToOne
    @JoinColumn(name = "books_id")
    private Books books;

    @Enumerated(EnumType.STRING) //ENUM으로 설정함. 이외의 다른 설정을 불가하도록 만들기 위해.
    private TransactionStatus status; //거래 상태 (신규등록, 수거중, 검수중, 검수완료(나눔중), 나눔완료)
    private LocalDateTime successDate; //거래 완료(성사) 날짜

    public void setBooks(Books books){
        this.books = books;
    }

}


