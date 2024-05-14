package profit.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class TokenRedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Redis에 refresh token 저장
    public void saveToken(String key, String refreshToken) {
        redisTemplate.opsForValue().set(key, refreshToken);
    }

    // Redis에서 refresh token 검색
    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Redis에서 refresh token 삭제
    public void deleteRefreshToken(String key) {
        redisTemplate.delete(key);
    }

    // Redis에 인증 코드 저장
    public void saveCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 3, TimeUnit.MINUTES); // 3분 동안 유효
    }

    // Redis에서 인증 코드 가져오기
    public String getCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }

    // Redis에서 인증 코드 삭제
    public void deleteCode(String email) {
        redisTemplate.delete(email);
    }
}
