package profit.login.controller;


import org.springframework.web.bind.annotation.*;

//@CrossOrigin
@RestController
public class TestController {

    @GetMapping("/test")
    public String getString(@RequestParam String input) {
        return "Received: " + input;
    }
}
