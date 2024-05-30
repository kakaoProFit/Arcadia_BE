package profit.login.question_board.response;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentWriteResponse {
    private String message;
    private String nextUrl;
}