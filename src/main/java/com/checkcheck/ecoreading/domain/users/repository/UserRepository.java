package com.checkcheck.ecoreading.domain.users.repository;

import com.checkcheck.ecoreading.domain.users.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByEmail(String email);

    // 유저 id 찾기 (???) 임시..
    Users findUsersByNickName(String nickName);
}
