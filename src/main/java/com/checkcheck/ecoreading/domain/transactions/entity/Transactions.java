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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @ManyToOne
    @JoinColumn(name = "giver_id")
    private Users giver;

    @ManyToOne
    @JoinColumn(name = "taker_id")
    private Users taker;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Books book;

    private String status;

    private LocalDateTime transactionDate;



    //private String userStatus;

}