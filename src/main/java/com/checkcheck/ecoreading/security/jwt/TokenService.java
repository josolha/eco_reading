package com.checkcheck.ecoreading.security.jwt;

import com.checkcheck.ecoreading.domain.users.service.RedisService;
import com.checkcheck.ecoreading.domain.users.service.UserService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final TokenProvider tokenProvider;
    private final RedisService redisService;
    private final UserService userService;

    public String createNewAccessToken(String refreshToken){
        //토근 유호성 검사에 실패하면 예외 발생
        if(!tokenProvider.validToken(refreshToken)){
            throw new IllegalStateException("Unexpected token");
        }
        //Todo : 레디스에서 찾아서 이메일 가져오기
        //Long userId = redisService
        //Todo : 유저서비스에서 그 아이디 값을 찾아보기
        //그걸 user 로 던지기 userService.findById(userId);

        //return tokenProvider.generateToken(user, Duration.ofHours(2));
        return null;
    }

}
