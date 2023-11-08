package com.checkcheck.ecoreading.domain.users.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class Users extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userName;
    private String nickName;
    private String password;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String phone;
    private String postcode;
    private String roadAddress;
    private String detailAddress;
    private int totalPoint;

    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean enabled = true;
    private String socialAuth;
    private String socialAuthId;
    private Boolean emailVerified = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }

     /*
       스프링 시큐리티는 인증 과정에서 UserDetails 객체의 getUsername() 메서드를 호출하여 사용자의 고유 식별자를 얻는다.
       이 식별자는 보통 로그인에 사용되는 유저네임(username)이나 이메일 주소(email)일 수 있다.
       UserDetails의 getUsername() 메서드가 반환하는 값은 UserDetailsService 인터페이스를 구현하는 서비스에서
       loadUserByUsername(String username) 메서드를 호출할 때 전달되는 파라미터와 일치해야 한다.
     */
    @Override
    public String getUsername(){
        return email;
    }
    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}