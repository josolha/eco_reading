package com.checkcheck.ecoreading.security.jwt;


import com.checkcheck.ecoreading.domain.users.dto.UserResponseDTO;
import com.checkcheck.ecoreading.domain.users.entity.Users;
import com.checkcheck.ecoreading.domain.users.repository.UserRepository;
import com.checkcheck.ecoreading.domain.users.service.CustomUserDetails;
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
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
   // private static final long ACCESS_TOKEN_EXPIRE_TIME = 10 * 1000L; // 10초

    private static final long ACCESS_TOKEN_EXPIRE_TIME = 30 * 60 * 1000L;              // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일
    private final UserCustomDetailService userDetailsService;
    private final RedisService redisService;

    private final Key key;

    public JwtTokenProvider(UserCustomDetailService userDetailsService, RedisService redisService, @Value("${jwt.secret}") String secretKey) {
        this.redisService = redisService;
        this.userDetailsService = userDetailsService;
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 주어진 인증 정보를 바탕으로 액세스 토큰과 리프레시 토큰을 생성한다.
     */
    public UserResponseDTO.TokenInfo generateToken(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getUserId();
        String userEmail = extractUsername(authentication);
        String authorities = extractAuthorities(authentication);
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = buildToken(userEmail, authorities, accessTokenExpiresIn,userId);
        String refreshToken = buildToken(userEmail, authorities, new Date(now + REFRESH_TOKEN_EXPIRE_TIME),userId);
        saveRefreshTokenInRedis(userEmail, refreshToken);
        return new UserResponseDTO.TokenInfo(accessToken, ACCESS_TOKEN_EXPIRE_TIME, refreshToken, REFRESH_TOKEN_EXPIRE_TIME);
    }

    /**
     * Authentication 객체에서 사용자 이름을 추출한다.
     */
    private String extractUsername(Authentication authentication) {
        return ((UserDetails) authentication.getPrincipal()).getUsername();
    }

    /**
     * Authentication 객체에서 권한 목록을 문자열로 추출한다.
     */
    private String extractAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    /**
     * 주어진 정보를 바탕으로 JWT 토큰을 생성한다.
     */
    private String buildToken(String subject, String authorities, Date expiration, Long userId) {
        return Jwts.builder()
                .setSubject(subject)
                .claim(AUTHORITIES_KEY, authorities)
                .claim("userId", userId) // userId 클레임 추가
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * 리프레시 토큰을 Redis에 저장한다.
     */
    private void saveRefreshTokenInRedis(String username, String refreshToken) {
        String refreshTokenKey = "RefreshToken:" + username;
        redisService.setValues(refreshTokenKey, refreshToken, Duration.ofMillis(REFRESH_TOKEN_EXPIRE_TIME));
    }

    /**
     * 주어진 액세스 토큰으로부터 인증 정보를 추출한다.
     */
    public Authentication getAuthentication(String accessToken) {
        Claims claims = extractClaims(accessToken, "Failed to extract authentication info from access token");
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(claims);

        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    /**
     * 토큰에서 주제(subject)를 추출한다.
     */
    public String getSubject(String token) {
        Claims claims = extractClaims(token, "Failed to extract subject from token");
        return claims.getSubject();
    }

    /**
     * 토큰에서 userId를 추출합니다.
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = extractClaims(token, "Failed to extract user ID from token");
        Number userId = claims.get("userId", Number.class); // 클레임에서 userId 추출
        return userId != null ? userId.longValue() : null;
    }

    /**
     * 토큰의 유효성을 검증한다.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            log.info("Invalid JWT Token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * 토큰에서 Claims 정보를 추출합니다. 만료된 토큰의 경우 ExpiredJwtException 처리가 포함한다.
     */
    private Claims extractClaims(String token, String errorMessage) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            throw new RuntimeException(errorMessage, e);
        }
    }

    /**
     * 사용자 이메일과 권한 정보를 바탕으로 새로운 액세스 토큰을 생성한다.
     */
    public String createAccessToken(String userEmail, Collection<? extends GrantedAuthority> authorities,Long userId) {
        // 사용자 이메일을 기반으로 userId 조회
        return buildToken(userEmail, extractAuthoritiesString(authorities), new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_TIME), userId);
    }

    /**
     * 토큰에서 권한 정보를 추출하여 GrantedAuthority의 컬렉션으로 반환한다.
     */
    public Collection<GrantedAuthority> getRoles(String token) {
        Claims claims = extractClaims(token, "Failed to extract roles from token");
        return extractAuthorities(claims);
    }

    /**
     * GrantedAuthority 컬렉션을 문자열로 변환한다.
     */
    private String extractAuthoritiesString(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    }

    /**
     * Claims 객체에서 권한 정보를 추출한다.
     */
    private Collection<GrantedAuthority> extractAuthorities(Claims claims) {
        return Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}