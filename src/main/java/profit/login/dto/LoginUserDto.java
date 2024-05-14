package profit.login.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

@Getter
@Setter
public class LoginUserDto {
    private String email;

    private String password;

    private String refreshToken;

    // getters and setters here...
    String setEmail(String email){
        this.email = email;
        return this.email;
    }

    String setPassword(String password){
        this.password = password;
        return this.password;
    }


}