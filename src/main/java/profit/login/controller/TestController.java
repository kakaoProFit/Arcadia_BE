package profit.login.controller;


import org.springframework.web.bind.annotation.*;

//@CrossOrigin
@RestController
public class TestController {

//    public String test;

    @GetMapping("/test")
    public String getString(@RequestParam String input) {
        return "Received: " + input;
    }
}
