package profit.login_rest_api_security_jwt.service;

import org.springframework.stereotype.Service;
import profit.login_rest_api_security_jwt.entity.User;
import profit.login_rest_api_security_jwt.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        try {
            // 사용자를 찾을 때 예외가 발생할 수 있으므로 try-catch 블록을 사용하여 예외를 처리합니다.
            userRepository.findAll().forEach(users::add);
        } catch (Exception e) {
            // 예외가 발생하면 클라이언트에게 적절한 오류 메시지를 반환합니다.

            throw new RuntimeException("Internal Server Error: Failed to find all users.");
        }

        return users;
    }
}