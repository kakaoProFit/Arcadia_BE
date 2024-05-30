package profit.login.question_board.dto;

//Nosql 형태로 저장할때 값을 따로 전송하기 위해서

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import profit.login.entity.User;
import profit.login.question_board.Entity.Board;
import profit.login.question_board.Entity.BoardCategory;

@Builder
@Getter
@Setter
@Document(collection = "test") //test에 저장, 나중에 표기 수정해서 applicaiton.yaml에서 수정할 수 있도록
public class BoardContentDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String Content;

    public BoardContentDto init(Long id, String body) {
        return BoardContentDto.builder()
                .id(id)
                .Content(body)
                .build();
    }
}


