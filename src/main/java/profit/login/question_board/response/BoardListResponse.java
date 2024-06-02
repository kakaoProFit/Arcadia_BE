package profit.login.question_board.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import profit.login.question_board.Entity.Board;
import profit.login.question_board.dto.BoardSearchRequest;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardListResponse {
    private String category;
    private Page<Board> boards;
    private BoardSearchRequest boardSearchRequest;
}