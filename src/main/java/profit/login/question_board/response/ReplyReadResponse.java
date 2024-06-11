package profit.login.question_board.response;

import lombok.Builder;
import lombok.Data;
import profit.login.entity.UserRole;
import profit.login.question_board.Entity.Comment;
import profit.login.question_board.Entity.Reply;

import java.util.List;

@Data
@Builder
public class ReplyReadResponse {
    private String message;
    private List<Long> userId;
    private String nickName;
    private List<Reply> reply;
}