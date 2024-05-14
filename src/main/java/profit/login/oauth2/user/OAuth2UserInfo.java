package profit.login.oauth2.user;

import java.util.Map;

public interface OAuth2UserInfo {

    OAuth2Provider getProvider();

    String getAccessToken();

    String getRefreshToken();

    Map<String, Object> getAttributes();

    String getId();

    String getEmail();

    String getName();

    String getPhoneNumber();

    String getBirthday();

    String getNickname();

    String getGender();

    String getProfileImageUrl();
}
