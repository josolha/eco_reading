package com.checkcheck.ecoreading.domain.transactions.repository;

import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    List<Transactions> findAll();

}
