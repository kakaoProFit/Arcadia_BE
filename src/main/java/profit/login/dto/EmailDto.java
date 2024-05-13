    package profit.login.dto;

    import jakarta.validation.constraints.NotNull;
    import lombok.Data;
    import lombok.Getter;
    import lombok.Setter;

    @Getter
    @Setter
    public class EmailDto {

        // 이메일 주소

        private String mail;
        // 인증 코드
        private String verifyCode;
        // 이메일 주소 반환
        public String getMail() {
            return this.mail;
        }


    }