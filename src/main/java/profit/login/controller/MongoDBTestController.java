package profit.login.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import profit.login.service.MongoDBTestService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/mongo")
public class MongoDBTestController {

    @Autowired
    MongoDBTestService mongoDBTestService;

    @PostMapping(value = "/find")
    public String findUserData(@RequestBody Map<String, Object> payload) {
        String name = (String) payload.get("name");
        return mongoDBTestService.selectUser(name);
    }

    @PostMapping(value = "/save")
    public String saveUserData(@RequestBody Map<String, Object> payload) {
        String name = (String) payload.get("name");
        int age = (int) payload.get("age");

        log.info("[Controller][Recv] name : {}, age : {}", name, age);
        mongoDBTestService.saveUser(name, age);

        return mongoDBTestService.selectUser(name);
    }
}
