package profit.login.dto;

import lombok.Builder;
import lombok.Data;
import profit.login.entity.User;

//정보 수정, 탈퇴 등에 사용되는 DTO
@Data
@Builder
public class ChangeUserDto {

    private Long loginId;
    private String fullName;
    private String password;
    private String newPassword;
    private String birth;
    private String phone;
    private String description;

    public static ChangeUserDto of(User user) {
        return ChangeUserDto.builder()
                .loginId(user.getId())
//                .password(user.getPassword())
                .fullName(user.getFullName())
                .birth(user.getBirth())
                .phone(user.getPhone())
                .description(user.getDescription())
                .build();
    }
}