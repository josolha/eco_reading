package com.checkcheck.ecoreading.domain.users.repository;

import com.checkcheck.ecoreading.domain.users.entity.Users;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByEmailAndSocialAuthIsNull(String email);
    Optional<Users> findByEmailAndSocialAuthId(String email, Long socialAuthId);
    Optional<Users> findByUserNameAndPhone(String name,String phone);


    Users findAllByUsersId(Long userId);
    List<Users> findAll();
    // 유저 id 찾기 (???) 임시..
    Users findUsersByNickName(String nickName);
    Optional<Users> findBySocialAuthId(Long socialAuthId);

    Integer findTotalPointByUsersId(Long usersId);

    Optional<Users> findByEmail(String email);
}
