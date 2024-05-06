package profit.login_rest_api_security_jwt.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {

    // Getters and setters...
    // Getter 메서드
    // Setter 메서드
    private String token;

    private long expiresIn;
}