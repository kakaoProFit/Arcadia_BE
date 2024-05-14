package profit.login.oauth2.user;

import java.util.Map;

public class NaverOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;
    private final String accessToken;
    private final String refreshToken;
    private final String id;
    private final String email;
    private final String name;

    private final String nickName;
    private final String profileImageUrl;

    private final String phoneNumber;
    private final String birthday;

    private final String gender;

    public NaverOAuth2UserInfo(String accessToken, String refreshToken, Map<String, Object> attributes) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        // attributes 맵의 response 키의 값에 실제 attributes 맵이 할당되어 있음
        this.attributes = (Map<String, Object>) attributes.get("response");
        this.id = (String) this.attributes.get("id");
        this.email = (String) this.attributes.get("email");
        this.name = (String) this.attributes.get("name");
        this.nickName = (String) this.attributes.get("nickname");
        ;
        this.profileImageUrl = (String) attributes.get("profile_image");

        this.phoneNumber = (String) this.attributes.get("mobile");
        this.birthday = (String) this.attributes.get("birthday");
        this.gender = (String) this.attributes.get("gender");
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.NAVER;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String getRefreshToken() { return refreshToken; }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPhoneNumber() { return phoneNumber; }

    @Override
    public String getBirthday() { return birthday; }

    @Override
    public String getNickname() {
        return nickName;
    }

    @Override
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    @Override
    public String getGender() {return gender;}
}
