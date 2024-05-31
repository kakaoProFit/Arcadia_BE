package profit.login.controller;


import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

    @CrossOrigin(origins = "*", methods= RequestMethod.GET)
    @GetMapping("/test")
    public String getString(@RequestParam String input) {
        return "Received: " + input;
    }
}
