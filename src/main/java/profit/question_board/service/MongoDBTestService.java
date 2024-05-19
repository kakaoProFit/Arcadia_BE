package profit.question_board.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import profit.question_board.repository.MongoDBTestRepository;


@Slf4j
@Component
public class MongoDBTestService {

    @Autowired
    MongoDBTestRepository mongoDBTestRepository;

    public String selectBoard(String title) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (mongoDBTestRepository.findByTitle(title) == null) {
                log.info("[Service] user name : {} not exist!!", title);
                return String.format("user name : %s not exist!!", title);
            } else {
                return objectMapper.writeValueAsString(mongoDBTestRepository.findByTitle(title));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

//    public void saveUser(String name, int age) {
//
//        MongoDBTestModel mongoDBTestModel = new MongoDBTestModel();
//        mongoDBTestModel.setName(name);
//        mongoDBTestModel.setAge(age);
//
//        if (mongoDBTestRepository.findByName(name) != null) {
//            log.info("[Service][update] name is already exist!!");
//            mongoDBTestModel.setId(mongoDBTestRepository.findByName(name).getId());
//        } else {
//            log.info("[Service][insert] New name received!!");
//        }
//
//        mongoDBTestRepository.save(mongoDBTestModel);
//    }
}
