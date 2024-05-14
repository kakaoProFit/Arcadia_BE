package profit.login.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {

    // Getters and setters...
    // Getter 메서드
    // Setter 메서드
    private String accestoken;

    private String refreshtoken;

    private long expiresIn;
}