package profit.login.question_board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

//게시글 리스트에서 검색할 때 사용하는 DTO

@Data
@AllArgsConstructor
public class BoardSearchRequest {

    private String sortType;
    private String searchType;
    private String keyword;
}