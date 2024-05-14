package profit.login.controller;

import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import profit.login.dto.EmailCheckReq;
import profit.login.dto.LoginUserDto;
import profit.login.dto.RegisterUserDto;
import profit.login.entity.User;
import profit.login.response.LoginResponse;
import profit.login.service.AuthenticationService;
import profit.login.service.EmailService;
import profit.login.service.JwtService;
import profit.login.service.TokenRedisService;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;



@RequestMapping("/auth")
@RestController
@Slf4j
public class AuthenticationController {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRedisService tokenRedisService;

    private final AuthenticationService authenticationService;

    private final EmailService emailService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserDetailsService userDetailsService, TokenRedisService tokenRedisService, EmailService emailService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userDetailsService = userDetailsService;
        this.tokenRedisService = tokenRedisService;
        this.emailService = emailService;
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

    //인증메일 발송
    @PostMapping("/sign-up/emailCheck")
    public ResponseEntity<Map<String, String>> emailCheck(@RequestBody EmailCheckReq emailCheckReq) throws MessagingException, UnsupportedEncodingException {
        log.info(emailCheckReq.getEmail());
        String authCode = emailService.sendEmail(emailCheckReq.getEmail());
        String email = emailCheckReq.getEmail();
        log.info("getEmail: " + email);
        log.info("authCode: " + authCode);
        // refresh token과 access token을 Redis에 저장
        tokenRedisService.saveCode(email, authCode);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("authCode", authCode);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/sign-up/verify")
    public ResponseEntity<Map<String, String>> emailVerify(@RequestBody EmailCheckReq emailCheckReq) throws MessagingException, UnsupportedEncodingException{
        String emailCode  = tokenRedisService.getCode(emailCheckReq.getEmail());

        if(emailCode.equals(emailCheckReq.getEmailcode())){
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("emailCode: ", emailCode);
            log.info("인증되었습니다");
            return ResponseEntity.ok(responseBody);
        }
        else {
            log.info("이메일이 일치하지 않습니다.");
            return ResponseEntity.notFound().build();

        }
    }





}