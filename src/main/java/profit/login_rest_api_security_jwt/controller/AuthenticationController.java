package profit.login_rest_api_security_jwt.controller;

import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import profit.login_rest_api_security_jwt.entity.User;
import profit.login_rest_api_security_jwt.dto.LoginUserDto;
import profit.login_rest_api_security_jwt.response.LoginResponse;
import profit.login_rest_api_security_jwt.dto.RegisterUserDto;
import profit.login_rest_api_security_jwt.service.AuthenticationService;
import profit.login_rest_api_security_jwt.service.JwtService;
import org.springframework.http.ResponseEntity;
import profit.login_rest_api_security_jwt.service.TokenRedisService;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRedisService tokenRedisService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserDetailsService userDetailsService, TokenRedisService tokenRedisService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userDetailsService = userDetailsService;
        this.tokenRedisService = tokenRedisService;
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String accessToken = jwtService.generateToken(authenticatedUser);
        String refreshToken = jwtService.generateRefreshToken(authenticatedUser);

        // refresh token과 access token을 Redis에 저장
        tokenRedisService.saveToken(String.valueOf(authenticatedUser.getId()), refreshToken);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccestoken(accessToken);
        loginResponse.setRefreshtoken(refreshToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/refresh/{id}")
    public ResponseEntity<Map<String, String>> refreshAccessToken(@RequestBody Map<String, String> refreshTokenRequest, @PathVariable Integer id) {
        String refreshToken = refreshTokenRequest.get("refreshToken");

        // RefreshToken을 사용하여 UserDetails를 가져옵니다.
        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtService.extractUsername(refreshToken));

        // 새로운 AccessToken과 RefreshToken을 생성합니다.
        String newAccessToken = jwtService.generateToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);

        tokenRedisService.saveToken(String.valueOf(id), newRefreshToken);
        String NewSavedRefreshToken = tokenRedisService.getRefreshToken(String.valueOf(id));

        // 새로운 AccessToken과 RefreshToken을 클라이언트에게 반환합니다.
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        response.put("refreshToken", newRefreshToken);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/refresh-token/{id}")
    public ResponseEntity<String> getRefreshToken(@PathVariable String id) {
        String refreshToken = tokenRedisService.getRefreshToken(id);
        if (refreshToken != null) {
            return ResponseEntity.ok(refreshToken);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



}