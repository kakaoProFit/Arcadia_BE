package profit.login.dto;

import lombok.Builder;
import lombok.Data;
import profit.login.entity.User;

//정보 수정, 탈퇴 등에 사용되는 DTO
@Data
@Builder
public class ChangeUserDto {

    private String loginId;
    private String nickname;
    private String nowPassword;
    private String newPassword;
    private String newPasswordCheck;

    public static ChangeUserDto of(User user) {
        return ChangeUserDto.builder()
                .loginId(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }
}