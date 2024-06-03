package profit.login.question_board.dto;

//댓글을 입력받아 DB에 저장할 때 사용하는 DTO

import lombok.Data;
import profit.login.entity.UserRole;
import profit.login.question_board.Entity.Comment;
import profit.login.entity.User;
import profit.login.question_board.Entity.Board;

@Data
public class CommentCreateRequest {

    private String body;

    public Comment toEntity(Board board, User user) {
        return Comment.builder()
                .user(user)
                .board(board)
                .body(body)
                .build();
    }
}