package profit.login.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import profit.login.dto.ChangeUserDto;
import profit.login.dto.UpdatePasswordDto;
import profit.login.entity.User;
import profit.login.repository.UserRepository;
import profit.login.service.UserService;


@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    // 유저 정보 조회 API
    @GetMapping("/read/{userId}")
    public ResponseEntity<User> getUserInfo(@PathVariable Long userId) {
        User userInfo = userService.getUserInfo(userId);
        return ResponseEntity.ok(userInfo);
    }
    @PutMapping("/update/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long userId,
            @RequestBody ChangeUserDto changeUserDto) {

        User updatedUser = userService.updateUser(userId, changeUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody UpdatePasswordDto updatePasswordDto, Authentication authentication) {

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();


        userService.changePassword(user, updatePasswordDto.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }

}
