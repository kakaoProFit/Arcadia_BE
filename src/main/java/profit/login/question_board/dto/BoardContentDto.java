package profit.login.question_board.dto;

//Nosql 형태로 저장할때 값을 따로 전송하기 위해서

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "test") //test에 저장, 나중에 표기 수정해서 applicaiton.yaml에서 수정할 수 있도록
public class BoardContentDto {
    private String Content;
    private int UserId;
    private int BoardId;
}


