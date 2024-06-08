// ReployController.java
package profit.login.question_board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import profit.login.entity.User;
import profit.login.entity.UserRole;
import profit.login.question_board.dto.ReplyCreateRequest;
import profit.login.question_board.dto.PointRequest;
import profit.login.question_board.response.ReplyWriteResponse;
import profit.login.question_board.service.BoardService;
import profit.login.question_board.service.CommentService;
import profit.login.question_board.service.ReplyService;
import profit.login.repository.UserRepository;

import java.io.IOException;

@Controller
@RequestMapping("/reply")
@RequiredArgsConstructor
@Slf4j
public class ReplyController {

    private final CommentService commentService;
    private final BoardService boardService;
    private final ReplyService replyService;
    private final UserRepository userRepository;


    // 답변 작성
    @PostMapping("/write/{boardId}")
    public ResponseEntity<ReplyWriteResponse> commentWrite(@PathVariable Long boardId, @RequestBody ReplyCreateRequest req,
                                                           Authentication authentication) throws IOException {

        // 댓글 작성 서비스 호출
        replyService.writeReply(boardId, req, authentication.getName());

        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();
        UserRole userRole = user.getUserRole();

        String isExpert;

        if (userRole.equals((UserRole.EXPERT))){
            isExpert = "전문가입니다.";
        } else{
            isExpert = "일반유저입니다";
        }

        // 응답 메시지와 다음 URL 설정
        String message = "답글이 추가되었습니다.";
        String nextUrl = "/boards/" + boardService.getCategory(boardId) + "/" + boardId;

        // 응답 객체 생성
        ReplyWriteResponse response = ReplyWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .isExpert(isExpert)
                .userRole(userRole)
                .build();

    // // ResponseEntity로 응답 반환
     return ResponseEntity.ok(response);
     }

     // 답변 수정

    @PostMapping("/{replyId}/edit")
    public ResponseEntity<ReplyWriteResponse> editReply(@PathVariable Long replyId, @RequestBody ReplyCreateRequest req,
                                                            Authentication authentication) {
        Long boardId = replyService.editReply(replyId, req.getBody(), authentication.getName());

        String message;
        String nextUrl;
        if (boardId == null) {
            message = "잘못된 요청입니다.";
            nextUrl = "/";
        } else {
            message = "답글이 수정 되었습니다.";
            nextUrl = "/boards/" + boardService.getCategory(boardId) + "/" + boardId;
        }

        ReplyWriteResponse response = ReplyWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .build();

     return ResponseEntity.ok(response);
     }


     // 답변 삭제
    @GetMapping("/{replyId}/delete")
    public ResponseEntity<ReplyWriteResponse> deleteComment(@PathVariable Long replyId, Authentication authentication) {
        Long boardId = replyService.deleteReply(replyId, authentication.getName());



        String message;
        String nextUrl;
        if (boardId == null) {
            message = "작성자만 삭제 가능합니다.";
            nextUrl = "/";
        } else {
            message = "댓글이 삭제 되었습니다.";
            nextUrl = "/boards/" + boardService.getCategory(boardId) + "/" + boardId;
        }

        ReplyWriteResponse response = ReplyWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .build();

        return ResponseEntity.ok(response);
    }

    // 답변채택

    @PostMapping("/select/{replyId}")
    public ResponseEntity<ReplyWriteResponse> selectReply(@PathVariable Long replyId, Authentication authentication) {




        replyService.selectReply(replyId, authentication.getName());

        ReplyWriteResponse response = ReplyWriteResponse.builder()
                .message("답변이 채택되었습니다.")
                .nextUrl("/boards/" + boardService.getCategory(replyId) + "/" + replyId)
                .build();

        return ResponseEntity.ok(response);
    }


}