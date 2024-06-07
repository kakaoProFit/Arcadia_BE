package profit.login.oauth2.handler;

import io.lettuce.core.AbstractRedisAsyncCommands;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import profit.login.dto.RegisterUserDto;
import profit.login.dto.SocialRegisterUserDto;
import profit.login.entity.User;
import profit.login.jwt.TokenProvider;
import profit.login.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import profit.login.oauth2.service.OAuth2UserPrincipal;
import profit.login.oauth2.user.OAuth2Provider;
import profit.login.oauth2.user.OAuth2UserInfo;
import profit.login.oauth2.user.OAuth2UserUnlinkManager;
import profit.login.oauth2.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import profit.login.repository.UserRepository;
import profit.login.dto.SocialRegisterUserDto;
import profit.login.service.JwtService;
import profit.login.service.TokenRedisService;

import java.io.IOException;
import java.util.Optional;

import static profit.login.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.MODE_PARAM_COOKIE_NAME;
import static profit.login.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
@Service
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final OAuth2UserUnlinkManager oAuth2UserUnlinkManager;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final TokenRedisService tokenRedisService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        String targetUrl;

        targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {

        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        String mode = CookieUtils.getCookie(request, MODE_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse("");

        OAuth2UserPrincipal principal = getOAuth2UserPrincipal(authentication);

        if (principal == null) {
            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("error", "Login failed")
                    .build().toUriString();
        }

        if ("login".equalsIgnoreCase(mode)) {

            log.info("email={}, name={}, nickname={}, phoneNumber={}, birthday={}, gender={}, accessToken={}, refreshToken={}", principal.getUserInfo().getEmail(),
                    principal.getUserInfo().getName(),
                    principal.getUserInfo().getNickname(),
                    principal.getUserInfo().getPhoneNumber(),
                    principal.getUserInfo().getBirthday(),
                    principal.getUserInfo().getGender(),
                    principal.getUserInfo().getAccessToken(),
                    principal.getUserInfo().getRefreshToken()
            );

            String email = principal.getUserInfo().getEmail();
            Optional<User> existingUser = userRepository.findByEmail(email);

            User user;

            if (existingUser.isEmpty()) {
                // 사용자가 존재하지 않으면 새로운 User 객체 생성 및 저장
                user = new User();
                user.setFullName(principal.getUserInfo().getName());
                user.setEmail(email);
                user.setPassword("SOCIAL");
                user.setBirth(principal.getUserInfo().getBirthday());
                user.setPhone(principal.getUserInfo().getPhoneNumber());

                userRepository.save(user);

            } else {
                user = existingUser.get();
                // 필요하면 기존 사용자 정보를 업데이트하는 로직 추가
                log.info("User already exists");
            }



            String accessToken = tokenProvider.createToken(authentication, String.valueOf(user.getId()));
            String refreshToken = tokenProvider.createRefreshToken(authentication, String.valueOf(user.getId()));

            tokenRedisService.saveToken(String.valueOf(user.getId()), refreshToken);

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .queryParam("access_token", accessToken)
                    .queryParam("refresh_token", refreshToken)
                    .build().toUriString();


        } else if ("unlink".equalsIgnoreCase(mode)) {

            String email = principal.getUserInfo().getEmail();
            String accessToken = principal.getUserInfo().getAccessToken();
            String refreshToken = principal.getUserInfo().getRefreshToken();
            OAuth2Provider provider = principal.getUserInfo().getProvider();

            userRepository.findByEmail(email).ifPresent(user -> {
                userRepository.delete(user);
                log.info("User with email {} deleted", email);
            });

            oAuth2UserUnlinkManager.unlink(provider, accessToken, refreshToken);

            return UriComponentsBuilder.fromUriString(targetUrl)
                    .build().toUriString();
        }

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", "Login failed")
                .build().toUriString();
    }


    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }
        return null;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
