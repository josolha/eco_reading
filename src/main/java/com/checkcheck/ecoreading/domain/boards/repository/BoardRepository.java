package com.checkcheck.ecoreading.domain.boards.repository;

import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Boards, Long> {

    List<Boards> findAllByUsers(Users users);
    Boards findAllByBoardId(Long boardId);
    List<Boards> findAll();
}
