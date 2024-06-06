package profit.login.question_board.dto;

import profit.login.question_board.Entity.Board;


import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import profit.login.question_board.Entity.BoardCategory;
import profit.login.question_board.Entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BoardDto {

    private Long id;
    private String userLoginId;
    private String userNickname;
    private String title;
    private String body;
    private Integer likeCnt;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private List<Comment> comments;
    private BoardCategory category;

    public static BoardDto of(Board board) {
        return BoardDto.builder()
                .id(board.getId())
                .userLoginId(board.getUser().getEmail())
                .userNickname(board.getUser().getNickname())
                .title(board.getTitle())
                .body(board.getBody())
                .createdAt(board.getCreatedAt())
                .lastModifiedAt(board.getLastModifiedAt())
                .likeCnt(board.getLikes().size())
                .comments(board.getComments())
                .category(board.getCategory())

                .build();
    }
}