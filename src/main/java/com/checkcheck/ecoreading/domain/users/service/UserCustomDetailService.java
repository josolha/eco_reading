package com.checkcheck.ecoreading.domain.users.service;

import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.exception.AuthenticationEmailException;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserCustomDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Users> user = userRepository.findByEmail(email);
        System.out.println("user ========== " + user);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("해당 이메일로 등록된 사용자가 없습니다: " + email);
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(user.get().getRole().name());
        CustomUserDetails userDetails;
        if (user.get().getSocialAuth() != null) {
            // 소셜 로그인 사용자 처리
            userDetails = new CustomUserDetails(user.get().getUsersId(), user.get().getEmail(), "", Collections.singleton(authority));
        } else {
            // 일반 로그인 사용자 처리
            userDetails = new CustomUserDetails(user.get().getUsersId(), user.get().getEmail(), user.get().getPassword(), Collections.singleton(authority));
        }
        return userDetails;
    }
}

