package com.checkcheck.ecoreading.domain.books.repository;

import com.checkcheck.ecoreading.domain.books.entity.Books;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Books, Long> {

    List<Books> findAll();
    Books findByBooksId(Long bookId);

}
