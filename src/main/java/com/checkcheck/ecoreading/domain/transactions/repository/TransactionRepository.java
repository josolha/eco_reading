package com.checkcheck.ecoreading.domain.transactions.repository;

import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactions, Long> {
//    List<Integer> findBook_idByTaker(Long taker);
    List<Transactions> findByTaker(Long taker);
}
