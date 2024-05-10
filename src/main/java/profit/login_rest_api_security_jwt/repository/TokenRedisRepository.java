package profit.login_rest_api_security_jwt.repository;

import profit.login_rest_api_security_jwt.dto.TokenRedis;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
public interface TokenRedisRepository extends CrudRepository<TokenRedis, String> {

    Optional<TokenRedis> findByAccessToken(String accessToken); // AccessToken으로 찾아내기
}
