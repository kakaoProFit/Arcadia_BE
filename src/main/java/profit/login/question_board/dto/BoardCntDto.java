package profit.login.question_board.dto;

//홈 화면에서 각각의 카테고리에 해당하는 Board 수를 출력하기 위해 사용되는 DTO

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardCntDto {
    // 쓸거면 나중에 일기, 질문, 자유로 나누면 될듯.
    private Long totalNoticeCnt;
    private Long totalBoardCnt;
    private Long totalQuestionCnt;
    private Long totalFreeCnt;
    private Long totalInformCnt;
    private Long totalDiaryCnt;
}