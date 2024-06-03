package profit.login.question_board.dto;

//댓글을 입력받아 DB에 저장할 때 사용하는 DTO

import lombok.Data;
import profit.login.question_board.Entity.Comment;
import profit.login.entity.User;
import profit.login.question_board.Entity.Board;
import profit.login.question_board.Entity.Reply;

@Data
public class ReplyCreateRequest {

    private String body;

    public Reply toEntity(Board board, User user) {
        return Reply.builder()
                .user(user)
                .board(board)
                .likeCnt(0)
                .body(body)
                .build();
    }
}