package com.checkcheck.ecoreading.domain.boards.repository;


import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Boards, Long> {

    List<Boards> findAllByUsers(Users users);
    Boards findAllByBoardId(Long boardId);
    List<Boards> findAll();
    int deleteAllByBoardId(Long boardId);
    Page<Boards> findAllByUsers(Users users, Pageable pageable);
}
