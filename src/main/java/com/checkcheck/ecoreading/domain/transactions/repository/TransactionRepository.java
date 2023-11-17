package com.checkcheck.ecoreading.domain.transactions.repository;

import com.checkcheck.ecoreading.domain.books.entity.Books;

import com.checkcheck.ecoreading.domain.transactions.entity.TransactionStatus;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    //List<Transactions> findAllByTakerId(Long takerId);
    Transactions findByBooks(Books books);

    Transactions findByTransactionsId(Long transactionsId);

//    List<Integer> findBook_idByTaker(Long taker);
    //List<Transactions> findByTaker(Long taker);

    // takeList 페이징 처리
    Page<Transactions> findAllByTakerId(Long takerId, Pageable pageable);
}
