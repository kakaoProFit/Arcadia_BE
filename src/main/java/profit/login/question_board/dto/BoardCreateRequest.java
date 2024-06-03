package profit.login.question_board.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import profit.login.entity.User;
import profit.login.question_board.Entity.Board;
import profit.login.question_board.Entity.BoardCategory;

@Data
@Getter
@Setter
public class BoardCreateRequest {

    private String title;
    private String body;
    private Integer point = 0;
    private MultipartFile uploadImage;

    public Board toEntity(BoardCategory category, User user) {
        return Board.builder()
                .user(user)
                .category(category)
                .title(title)
                .body(body)
                .point(point)
                .likeCnt(0)
                .commentCnt(0)
                .build();
    }
}