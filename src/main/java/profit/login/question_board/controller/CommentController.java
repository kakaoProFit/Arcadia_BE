// CommentController.java
package profit.login.question_board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import profit.login.question_board.dto.CommentCreateRequest;
import profit.login.question_board.response.CommentWriteResponse;
import profit.login.question_board.service.BoardService;
import profit.login.question_board.service.CommentService;

import java.io.IOException;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final BoardService boardService;

    @PostMapping("/write/{boardId}")
    public ResponseEntity<CommentWriteResponse> commentWrite(@PathVariable Long boardId,
            @RequestBody CommentCreateRequest req,
            Authentication authentication) throws IOException {

        // 댓글 작성 서비스 호출
        commentService.writeComment(boardId, req, authentication.getName());

        // 응답 메시지와 다음 URL 설정
        String message = "댓글이 추가되었습니다.";
        String nextUrl = "/boards/" + boardService.getCategory(boardId) + "/" + boardId;

        // 응답 객체 생성
        CommentWriteResponse response = CommentWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .build();

        // ResponseEntity로 응답 반환
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{commentId}/edit")
    public ResponseEntity<CommentWriteResponse> editComment(@PathVariable Long commentId,
            @RequestBody CommentCreateRequest req,
            Authentication authentication) {
        Long boardId = commentService.editComment(commentId, req.getBody(), authentication.getName());

        String message;
        String nextUrl;
        if (boardId == null) {
            message = "잘못된 요청입니다.";
            nextUrl = "/";
        } else {
            message = "댓글이 수정 되었습니다.";
            nextUrl = "/boards/" + boardService.getCategory(boardId) + "/" + boardId;
        }

        CommentWriteResponse response = CommentWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{commentId}/delete")
    public ResponseEntity<CommentWriteResponse> deleteComment(@PathVariable Long commentId,
            Authentication authentication) {
        Long boardId = commentService.deleteComment(commentId, authentication.getName());

        String message;
        String nextUrl;
        if (boardId == null) {
            message = "작성자만 삭제 가능합니다.";
            nextUrl = "/";
        } else {
            message = "댓글이 삭제 되었습니다.";
            nextUrl = "/boards/" + boardService.getCategory(boardId) + "/" + boardId;
        }

        CommentWriteResponse response = CommentWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .build();

        return ResponseEntity.ok(response);
    }
}