package profit.login.question_board.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LikedBoardResponse {
    private String message;
    private String nextUrl;
}

