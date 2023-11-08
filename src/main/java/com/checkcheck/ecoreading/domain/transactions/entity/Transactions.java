package com.checkcheck.ecoreading.domain.transactions.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "transactions")
public class Transactions extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long salesId;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Users userId;
//
//    @OneToOne
//    @JoinColumn(name = "book_id")
//    private Books book;
//    private String status;
//    private LocalDateTime salesDate;
//    private String userStatus;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private Long giver;
    private Long taker;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Books book;

    private String status;

    private LocalDateTime salesDate;

}


