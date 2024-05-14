package profit.login.oauth2.user;

import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

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

    public KakaoOAuth2UserInfo(String accessToken, String refreshToken, Map<String, Object> attributes) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        // attributes 맵의 kakao_account 키의 값에 실제 attributes 맵이 할당되어 있음
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
        this.attributes = kakaoProfile;

        this.id = ((Long) attributes.get("id")).toString();
        this.email = (String) kakaoAccount.get("email");
        this.phoneNumber = (String) kakaoAccount.get("phone_number");
        this.birthday = (String) kakaoAccount.get("birthday");

        this.name = (String) kakaoAccount.get("name");
        this.nickName = (String) kakaoAccount.get("profile_nickname");
        this.profileImageUrl = (String) attributes.get("profile_image_url");
        this.gender = (String) kakaoAccount.get("gender");

        this.attributes.put("id", id);
        this.attributes.put("email", this.email);
        this.attributes.put("nickname", this.nickName);
        this.attributes.put("phone_number", this.phoneNumber);
        this.attributes.put("birthday",this.birthday);
        this.attributes.put("gender",this.gender);
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.KAKAO;
    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

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
    public String getNickname() {
        return nickName;
    }

    @Override
    public String getPhoneNumber() { return phoneNumber; }

    @Override
    public String getBirthday() { return birthday; }

    @Override
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    @Override
    public String getGender() {return gender;}
}
