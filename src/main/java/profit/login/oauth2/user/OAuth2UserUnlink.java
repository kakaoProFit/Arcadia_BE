package profit.login.oauth2.user;

public interface OAuth2UserUnlink {
    void unlink(String accessToken, String refreshToken);
}
