package profit.login.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocialRegisterUserDto {
    private String email;

    private String fullName;

    public String setSocialEmail(String email){
        this.email = email;
        return this.email;
    }

    public String setSocialName(String fullName){
        this.fullName = fullName;
        return this.fullName;
    }
}
