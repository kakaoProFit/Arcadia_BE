package profit.login.question_board.response;

import lombok.Builder;
import lombok.Data;
import profit.login.entity.UserRole;

@Data
@Builder
public class ReplySelectResponse {
    private String message;
    private String nextUrl;
    private String isExpert;
    private UserRole userRole;
    private boolean selected;
}