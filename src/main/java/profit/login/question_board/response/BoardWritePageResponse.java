package profit.login.question_board.response;

import lombok.Builder;
import lombok.Data;
import profit.login.question_board.dto.BoardCreateRequest;

@Data
@Builder
public class BoardWritePageResponse {
    private String category;
    private BoardCreateRequest boardCreateRequest;

    // 생성자 추가
    public BoardWritePageResponse(String category, BoardCreateRequest boardCreateRequest) {
        this.category = category;
        this.boardCreateRequest = boardCreateRequest;
    }
}