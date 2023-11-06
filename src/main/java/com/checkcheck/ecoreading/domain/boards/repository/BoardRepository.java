package com.checkcheck.ecoreading.domain.boards.repository;

import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Boards, Long> {

}
