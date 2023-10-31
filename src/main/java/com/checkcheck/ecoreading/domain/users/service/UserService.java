package com.checkcheck.ecoreading.domain.users.service;

import com.checkcheck.ecoreading.domain.users.dto.AddUserRequest;
import com.checkcheck.ecoreading.domain.users.entity.Roles;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.entity.UsersRoles;
import com.checkcheck.ecoreading.domain.users.repository.RolesRepository;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import com.checkcheck.ecoreading.domain.users.repository.UsersRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final RolesRepository rolesRepository;
    private final UsersRolesRepository usersRolesRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long save(AddUserRequest dto) {
        Users user = Users.builder()
                .email(dto.getEmail())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .enabled(true)
                .emailVerified(false)
                .build();
        user = userRepository.save(user);
        Roles role = rolesRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("그런 룰은 없습니다."));
        UsersRoles usersRoles = UsersRoles.builder()
                .users(user)
                .roles(role)
                .build();
        // 사용자 역할 저장
        usersRolesRepository.save(usersRoles);
        return user.getId();
    }

}
