package com.checkcheck.ecoreading.security.jwt;


import com.checkcheck.ecoreading.domain.users.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService; // UserDetailsService 주입

    public String generateToken(Users users, Duration expiredAT){
        Date now = new Date();
        return makeToken(new Date(now.getTime()+ expiredAT.toMillis()),users);
    }

    private String makeToken(Date expiry, Users users) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE) // 헤더 typ: JWT
                //내용 iss : josolha@gmail.com(properties 파일ㅇ에서 설정한 값)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now) // 내용 iat : 현재 시간
                .setExpiration(expiry) // 내용 exp : expiry 맴버 변숫값
                .setSubject(users.getEmail()) // 내용 sub : 유저의 이메일
                .claim("id",users.getId()) // 클레임 id : 유저 ID
                // 서명 : 비밀값과 함께 해시값을 HS256 방식으로 암호화
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    // JWT 토큰 유호성 검증 메서드
    public boolean validToken(String token){
        try{
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey()) // 비밀값으로 복호화
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){ //복호화 과정에서 에러가 나면 유호하지 않은 토큰
            return false;
        }
    }
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        // 토큰에서 사용자의 정보를 찾습니다.
        UserDetails users = userDetailsService.loadUserByUsername(claims.getSubject());

        // UsernamePasswordAuthenticationToken 생성
        return new UsernamePasswordAuthenticationToken(
                users, // 이미 UserDetails를 구현한 Users 객체 사용
                null, // 비밀번호는 인증 후에는 필요 없으므로 null 처리
                users.getAuthorities() // UserDetails에서 권한 정보 가져오기
        );
    }

    //토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token){
        Claims claims = getClaims(token);
        return claims.get("id",Long.class);
    }

    private Claims getClaims(String token){
        return Jwts.parser() // 클레임 조회
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
    }
}
