package profit.login.dto;

// MONGODB 테스트 하고 지울 코드, 해당 코드로 빌드 오류시 관련 코드 모두 삭제해도 기능에 이상없음

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "test")
public class MongoDBTestModel {

    private String name;
    private int Id;
    private int age;
}