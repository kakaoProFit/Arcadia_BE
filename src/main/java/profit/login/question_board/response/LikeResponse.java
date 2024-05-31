package profit.login.question_board.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeResponse {
    private String message;
    private String nextUrl;
}
