package profit.login.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import profit.login.dto.ChangeUserDto;
import profit.login.entity.User;
import profit.login.service.UserService;


@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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

}
