package com.checkcheck.ecoreading.domain.users.repository;



import com.checkcheck.ecoreading.domain.users.entity.Users;


import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<Users,Long> {
    Optional<Users> findByEmailAndSocialAuthIsNull(String email);
    Optional<Users> findByEmailAndSocialAuthId(String email, Long socialAuthId);
    Optional<Users> findByUserNameAndPhone(String name,String phone);


    Users findAllByUsersId(Long userId);

    
//    Page<Users> findAllPage(Pageable pageable);
    // 페이징 처리 실패해서 주석처리
    Page<Users> findAll(Pageable pageable);


    // 유저 id 찾기 (???) 임시..
    Page<Users> findByEnabled(Boolean enabled, Pageable pageable);

    Users findUsersByNickName(String nickName);
    Optional<Users> findBySocialAuthId(Long socialAuthId);

    Integer findTotalPointByUsersId(Long usersId);

    Optional<Users> findByEmail(String email);
}
