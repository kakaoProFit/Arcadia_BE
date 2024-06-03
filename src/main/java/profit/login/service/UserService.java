package profit.login.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import profit.login.entity.User;
import profit.login.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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






//    @Transactional
//    public void changeRole(String id) {
//        User user = userRepository.findById(id).get();
//        // admin 페이지 만들거면 Role을 일반과 전문가로 이분화 하여서 진행해야함
//        //일단 예시로 만들어놓음.
//        user.changeRole();
//    }




}