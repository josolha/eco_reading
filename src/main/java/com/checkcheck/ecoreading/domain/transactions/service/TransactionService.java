package com.checkcheck.ecoreading.domain.transactions.service;

import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.boards.service.BoardService;
import com.checkcheck.ecoreading.domain.boards.service.BookService;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import com.checkcheck.ecoreading.domain.transactions.entity.TransactionStatus;
import com.checkcheck.ecoreading.domain.transactions.entity.Transactions;
import com.checkcheck.ecoreading.domain.transactions.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BookService bookService;
    private final BoardService boardService;


    public List<Transactions> findAll() {
        return transactionRepository.findAll();
    }
    public List<Boards> findAllByStatus(TransactionStatus statusInput){
        List<Transactions> transactions = transactionRepository.findAll();
        List<Transactions> transactionsSearchByStatus = new ArrayList<>();
        List<Books> books = new ArrayList<>();
        List<Boards> boards = new ArrayList<>();
        for (Transactions t : transactions){
            if (t.getStatus() == statusInput) transactionsSearchByStatus.add(t);
        }
        for (Transactions t : transactionsSearchByStatus){
           books.add(bookService.findBoardByBookId(t.getBooks().getBooksId()));
        }
        for (Books book : books){
            boards.add(boardService.findAllByBoardId(book.getBoards().getBoardId()));
        }
        return boards;
    }

    public Transactions findByBooks(Books books){
        return transactionRepository.findByBooks(books);
    }

    public void updateTransactionStatusFinishCheck(Transactions transactions){
        transactions.setStatus(TransactionStatus.수거중);
        transactionRepository.save(transactions);
    }

    public Transactions findByTransactionsId(Long transactionsId){
        return transactionRepository.findByTransactionsId(transactionsId);
    }

    public void updateTransactionsStatus(Transactions transactions){
        if (transactions.getStatus() == TransactionStatus.신규등록) transactions.setStatus(TransactionStatus.수거중);
        else if (transactions.getStatus() == TransactionStatus.수거중 || transactions.getStatus() == TransactionStatus.검수중) {
            transactions.setStatus(TransactionStatus.나눔중);
        }
        transactionRepository.save(transactions);
    }
}
