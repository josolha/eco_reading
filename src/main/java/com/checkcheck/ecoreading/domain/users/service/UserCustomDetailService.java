package com.checkcheck.ecoreading.domain.users.service;

import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.entity.UsersRoles;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserCustomDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            Users user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

            List<GrantedAuthority> authorities = new ArrayList<>();
            for (UsersRoles usersRole : user.getUsersRoles()) {
                authorities.add(new SimpleGrantedAuthority(usersRole.getRoles().getName()));
            }

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);

        } catch (UsernameNotFoundException e) {
            log.info("등록된 이메일이 없습니다...");
            throw e;  // 예외를 다시 던져서 Spring Security가 처리할 수 있도록 한다.
        }
    }

}
