package com.checkcheck.ecoreading.domain.users.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@Column(nullable = false, unique = true)
    private String username;

    //@Column(nullable = false)
    private String password;

    //@Column(nullable = false, unique = true)
    private String email;

    //@Column(nullable = false)
    private Boolean enabled = true;

    private String socialAuth;

    //@Column(unique = true)
    private String socialAuthId;

    //@Column(nullable = false)
    private Boolean emailVerified = false;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UsersRoles> usersRoles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 정보가 CustomUserDetailsService 내에서 처리되므로 여기서는 비워둡니다.
        return new ArrayList<>();
    }

    // 스프링 시큐리티는 인증 과정에서 UserDetails 객체의 getUsername() 메서드를 호출하여 사용자의 고유 식별자를 얻습니다.
    // 이 식별자는 보통 로그인에 사용되는 유저네임(username)이나 이메일 주소(email)일 수 있습니다. UserDetails의 getUsername()
    // 메서드가 반환하는 값은 UserDetailsService 인터페이스를 구현하는 서비스에서 loadUserByUsername(String username)
    // 메서드를 호출할 때 전달되는 파라미터와 일치해야 합니다.

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