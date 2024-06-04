package profit.login.question_board.response;

import lombok.*;
import profit.login.question_board.dto.BoardDto;

import java.util.List;

@Builder
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikedBoardListResponse {
    private String message;
    private List<BoardDto> boards;
}
