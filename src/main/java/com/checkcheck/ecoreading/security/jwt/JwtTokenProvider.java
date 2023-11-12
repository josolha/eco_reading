package com.checkcheck.ecoreading.security.jwt;


import com.checkcheck.ecoreading.domain.users.dto.UserResponseDTO;
import com.checkcheck.ecoreading.domain.users.service.RedisService;
import com.checkcheck.ecoreading.domain.users.service.UserCustomDetailService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 10 * 1000L; // 10초
    //private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;              // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일
    private final UserCustomDetailService userDetailsService;
    private final RedisService redisService;
    private final Key key;
    public JwtTokenProvider(UserCustomDetailService userDetailsService,RedisService redisService, @Value("${jwt.secret}") String secretKey) {
        this.redisService = redisService;
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하고 Redis 저장하는 메서드
    public UserResponseDTO.TokenInfo generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        log.info("========authorities========"+authorities);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername(); // 이메일이 username 필드에 저장되어 있다고 가정

        log.info("=========userEmail========"+userEmail);
        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(userEmail) // 여기에 사용자 이메일을 subject로 설정
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(userEmail) // 사용자 이메일을 subject로 설정
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        saveRefreshTokenInRedis(userEmail, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);

        return UserResponseDTO.TokenInfo.builder()
                .accessToken(accessToken)
                .accessTokenExpirationTime(ACCESS_TOKEN_EXPIRE_TIME)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }
    private void saveRefreshTokenInRedis(String username, String refreshToken, long refreshTokenExpirationTime) {
        String refreshTokenKey = "RefreshToken:" + username;
        redisService.setValues(refreshTokenKey, refreshToken, Duration.ofMillis(refreshTokenExpirationTime));
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        log.info("claims======"+claims);

        if (claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }
        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails users = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(users, "", authorities);
    }
    public String getSubject(String token) {
        log.info("token==="+ token);
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        log.info("expiration====="+expiration);
        // 현재 시간
        Long now = new Date().getTime();
        log.info("expiration -S ====="+ (expiration.getTime() - now));
        return (expiration.getTime() - now);
    }

    // 새로운 Access 토큰을 생성하는 메서드
    public String createAccessToken(String userEmail, Collection<? extends GrantedAuthority> authorities) {
        long now = (new Date()).getTime();

        String authoritiesString = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(userEmail) // 토큰에 사용자 이메일을 subject로 설정
                .claim(AUTHORITIES_KEY, authoritiesString) // 사용자 권한 목록을 클레임으로 설정
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME)) // 만료 시간 설정
                .signWith(key, SignatureAlgorithm.HS256) // HS256 알고리즘과 키를 사용하여 서명
                .compact(); // JWT 생성
    }
    public Collection<GrantedAuthority> getRoles(String token) {
        Claims claims = parseClaims(token);
        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}