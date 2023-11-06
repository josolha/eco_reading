package com.checkcheck.ecoreading.domain.books.repository;

import com.checkcheck.ecoreading.domain.books.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Books, Long> {
}
