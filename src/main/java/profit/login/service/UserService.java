package profit.login.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import profit.login.dto.ChangeUserDto;
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

    public User getUserInfo(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다. userId: " + userId);
        }

        return optionalUser.get();
    }

    public User updateUser(Long userId, ChangeUserDto changeUserDto) {
        // 유저 정보 조회
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("유저를 찾을 수 없습니다. userId: " + userId);
        }

        User user = optionalUser.get();

//        // 비밀번호 변경
//        if (changeUserDto.getNewPassword() != null && !changeUserDto.getNewPassword().isEmpty()) {
//            user.setPassword(changeUserDto.getNewPassword());
//        }

        // 닉네임 변경
        if (changeUserDto.getFullName() != null && !changeUserDto.getFullName().isEmpty()) {
            user.setFullName(changeUserDto.getFullName());
        }

        // 전화번호 변경
        if (changeUserDto.getPhone() != null && !changeUserDto.getPhone().isEmpty()) {
            user.setPhone(changeUserDto.getPhone());
        }

        // 생년월일 변경
        if (changeUserDto.getBirth() != null && !changeUserDto.getBirth().isEmpty()) {
            user.setBirth(changeUserDto.getBirth());
        }

        // 한줄 소개 변경
        if (changeUserDto.getDescription() != null && !changeUserDto.getDescription().isEmpty()) {
            user.setDescription(changeUserDto.getDescription());
        }

        // 변경된 유저 정보 저장
        return userRepository.save(user);
    }


}