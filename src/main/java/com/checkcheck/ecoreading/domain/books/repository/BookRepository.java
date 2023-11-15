package com.checkcheck.ecoreading.domain.books.repository;

import com.checkcheck.ecoreading.domain.books.entity.Books;
import java.util.List;

import com.checkcheck.ecoreading.domain.transactions.entity.TransactionStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
