package profit.login.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import profit.login.dto.LoginUserDto;
import profit.login.dto.RegisterUserDto;
import profit.login.entity.User;
import profit.login.repository.UserRepository;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRedisService tokenRedisService;
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;



    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            UserDetailsService userDetailsService,
            TokenRedisService tokenRedisService
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRedisService = tokenRedisService;

    }

    public User signup(RegisterUserDto input) {
        User user = new User();
        user.setFullName(input.getFullName());
        user.setEmail(input.getEmail());
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return userRepository.findByEmail(input.getEmail())
                .orElseThrow();
    }


    //향후 추가 구현해야함.
//    private final LikeRepository likeRepository;
//    private final CommentRepository commentRepository;
    @Transactional
    public void deleteUser(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                userRepository.delete(user);
            } else {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다.");
        }
    }


//    public LoginResponse authenticateLogin(LoginUserDto loginUserDto) {
//        loginUserDto.setRefreshToken(loginUserDto.getRefreshToken());
//        User authenticatedUser = authenticate(loginUserDto);
//
//        // 클라이언트가 제공한 리프레시 토큰과 Redis에서 가져온 해당 사용자의 리프레시 토큰을 비교
//        String clientRefreshToken = loginUserDto.getRefreshToken();
//        String redisRefreshToken = tokenRedisService.getRefreshToken(String.valueOf(authenticatedUser.getId()));
//
//        if (clientRefreshToken.equals(redisRefreshToken)) {
//            // 클라이언트가 제공한 리프레시 토큰과 Redis에서 가져온 리프레시 토큰이 일치할 경우
//            // 새로운 액세스 토큰을 생성하여 반환
//            String newAccessToken = jwtService.generateToken(authenticatedUser);
//            String newRefreshToken = jwtService.generateRefreshToken(authenticatedUser);
//
//            // Redis에 새로운 리프레시 토큰 저장
//            tokenRedisService.saveToken(String.valueOf(authenticatedUser.getId()), newRefreshToken);
//
//            LoginResponse loginResponse = new LoginResponse();
//            loginResponse.setAccestoken(newAccessToken);
//            loginResponse.setRefreshtoken(newRefreshToken);
//            loginResponse.setExpiresIn(jwtService.getExpirationTime());
//
//            return loginResponse;
//        } else {
//            // 리프레시 토큰이 일치하지 않는 경우
//            throw new RuntimeException("Refresh token mismatch");
//        }
//    }
}