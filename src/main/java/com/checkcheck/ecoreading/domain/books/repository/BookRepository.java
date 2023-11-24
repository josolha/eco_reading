package com.checkcheck.ecoreading.domain.books.repository;

import com.checkcheck.ecoreading.domain.books.entity.Books;
import java.util.List;

import com.checkcheck.ecoreading.domain.transactions.entity.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Books, Long> {

    List<Books> findAll();
    Books findByBooksId(Long bookId);
    List<Books> findByIsbn(String isbn);

    // 검색 메서드
//    List<Books> findByTitleContaining(String keyword);
//    List<Books> findByAuthorContaining(String keyword);
//    List<Books> findByPublisherContaining(String keyword);
//    List<Books> findByTitleContainingOrAuthorContainingOrPublisherContaining(String keyword, String keyword1, String keyword2);

    // 페이징된 도서 목록을 조회하는 메서드
    Page<Books> findAll(Pageable pageable);

    // 상태 나눔중인 나눔 글만 조회 및 페이징
    Page<Books> findAllByTransactions_Status(TransactionStatus status, Pageable pageable);

    // 검색 및 페이징 메서드
    Page<Books> findByTitleContaining(String keyword, Pageable pageable);
    Page<Books> findByAuthorContaining(String keyword, Pageable pageable);
    Page<Books> findByPublisherContaining(String keyword, Pageable pageable);
    Page<Books> findByTitleContainingOrAuthorContainingOrPublisherContaining(String keyword, String keyword1, String keyword2, Pageable pageable);

    // 검색 및 페이징 및 상태 나눔중인것만
    Page<Books> findByTitleContainingAndTransactions_Status(String keyword, TransactionStatus bookStatus, Pageable pageable);
    Page<Books> findByAuthorContainingAndTransactions_Status(String keyword, TransactionStatus bookStatus, Pageable pageable);
    Page<Books> findByPublisherContainingAndTransactions_Status(String keyword, TransactionStatus bookStatus, Pageable pageable);
    @Query("SELECT b FROM Books b " +
            "INNER JOIN Transactions t ON b.booksId = t.books.booksId " +
            "WHERE (b.title LIKE %:keyword% OR b.author LIKE %:keyword% OR b.publisher LIKE %:keyword%) " +
            "AND t.status = :bookStatus")
    Page<Books> searchBooksTransactions_Status(@Param("keyword") String keyword, @Param("bookStatus") TransactionStatus bookStatus, Pageable pageable);

}
