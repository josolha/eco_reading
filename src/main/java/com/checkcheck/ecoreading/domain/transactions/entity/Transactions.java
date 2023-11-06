package com.checkcheck.ecoreading.domain.transactions.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transactions extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesId;

    @ManyToOne
    @JoinColumn(name = "id")
    private Users takerId;

    @ManyToOne
    @JoinColumn(name = "id")
    private Users giverId;


    @OneToOne
    @JoinColumn(name = "book_id")
    private Books bookId;
    private String status;
    private LocalDateTime salesDate;

}
