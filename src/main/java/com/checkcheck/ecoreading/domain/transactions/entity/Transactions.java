package com.checkcheck.ecoreading.domain.transactions.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.delivery.entity.Delivery;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Table(name = "transactions")
public class Transactions extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long salesId;

    @Column(nullable = false)
    private Long giverId; //나눔글 올릴 시 giverID 넣기 필수

    private Long takerId;

    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @OneToOne
    @JoinColumn(name = "book_id")
    private Books books;

    @Enumerated //ENUM으로 설정함. 이외의 다른 설정을 불가하도록 만들기 위해.
    private TransactionStatus status; //거래 상태 (신규등록, 수거중, 검수중, 검수완료(나눔중), 나눔완료)
    private LocalDateTime salesDate;

    @Enumerated
    private UserStatus userStatus; //유저 상태 (유저가 기부어인지, 테이커인지)

}
