package com.checkcheck.ecoreading.domain.users.entity;

import com.checkcheck.ecoreading.domain.BaseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;

import com.checkcheck.ecoreading.domain.boards.entity.Boards;
import com.checkcheck.ecoreading.domain.books.entity.Books;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class Users extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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

    @Builder.Default
    @OneToMany(
            mappedBy = "users",
            cascade = CascadeType.ALL)
    private List<Boards> boardsList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean enabled = true;

    private String socialAuth;

    private String socialAuthId;

    private Boolean emailVerified = false;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 정보가 CustomUserDetailsService 내에서 처리되므로 여기서는 비워둔다.
        return new ArrayList<>();
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


    // 연관관계 메서드
    // 책 한 권을 올리면 Boards에 Book 추가되면서 Books엔티티에도 추가
    public void addBoard(Boards boards) {
        boards.setUsers(this);
        this.getBoardsList().add(boards);
    }
}