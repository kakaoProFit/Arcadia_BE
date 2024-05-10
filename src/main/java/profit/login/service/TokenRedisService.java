package profit.login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

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
}
