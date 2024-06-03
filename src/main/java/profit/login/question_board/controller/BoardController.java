package profit.login.question_board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import profit.login.dto.LoginUserDto;
import profit.login.entity.User;
import profit.login.entity.UserRole;
import profit.login.question_board.Entity.Board;
import profit.login.question_board.dto.*;
import profit.login.question_board.response.BoardListResponse;
import profit.login.question_board.response.ErrorResponse;
import profit.login.question_board.Entity.BoardCategory;
import profit.login.question_board.response.BoardWritePageResponse;
import profit.login.question_board.response.BoardWriteResponse;
import profit.login.question_board.service.BoardService;
import profit.login.question_board.service.UploadImageService;
import profit.login.question_board.service.LikeService;
import profit.login.question_board.service.CommentService;
import profit.login.repository.UserRepository;
import profit.login.service.AuthenticationService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final LikeService likeService;
    private final CommentService commentService;
    private final UploadImageService uploadImageService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;


    @GetMapping("/list/{category}")
    public ResponseEntity<?> boardListPage(@PathVariable String category,
                                           @RequestParam(required = false, defaultValue = "1") int page,
                                           @RequestParam(required = false) String sortType,
                                           @RequestParam(required = false) String searchType,
                                           @RequestParam(required = false) String keyword) {
        BoardCategory boardCategory = BoardCategory.of(category);
        if (boardCategory == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("카테고리가 존재하지 않습니다.", "/"));
        }


        PageRequest pageRequest = PageRequest.of(page-1 , 12, Sort.by("id").descending());
        if (sortType != null) {
            switch (sortType) {
                case "date":
                    pageRequest = PageRequest.of(page-1 , 12, Sort.by("createdAt").descending());
                    break;
                case "like":
                    pageRequest = PageRequest.of(page-1 , 12, Sort.by("likeCnt").descending());
                    break;
                case "comment":
                    pageRequest = PageRequest.of(page-1 , 12, Sort.by("commentCnt").descending());
                    break;
            }
        }


        Page<Board> boards = boardService.getBoardList(boardCategory, pageRequest, searchType, keyword);
        log.info("board's title: " + boards);
        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(sortType, searchType, keyword);

        BoardListResponse response = new BoardListResponse(category, boards, boardSearchRequest);

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/{category}/write")
//    public ResponseEntity<BoardWritePageResponse> boardWritePage(@PathVariable String category) {
//        BoardCategory boardCategory = BoardCategory.of(category);
//        // BoardCreateRequest를 사용하여 BoardWritePageResponse를 생성합니다.
//        BoardCreateRequest boardCreateRequest = new BoardCreateRequest();
//        if (boardCategory == null) {
//            return ResponseEntity.badRequest()
//                    .body(new BoardWritePageResponse("카테고리가 존재하지 않습니다.", boardCreateRequest));
//        }
//
//
//
//        // 생성된 BoardCreateRequest를 사용하여 BoardWritePageResponse를 생성합니다.
//        BoardWritePageResponse response = new BoardWritePageResponse(category, boardCreateRequest);
//        return ResponseEntity.ok(response);
//    }


    //게시물 작성
    @PostMapping("/write/{category}")
    public ResponseEntity<BoardWriteResponse> boardWrite(@PathVariable String category, @RequestBody BoardCreateRequest req, BoardContentDto bcd,
                                                         Authentication authentication) throws IOException {

        BoardCategory boardCategory = BoardCategory.of(category);
        if (boardCategory == null) {
            return ResponseEntity.badRequest().body(BoardWriteResponse.builder()
                    .message("카테고리가 존재하지 않습니다.")
                    .nextUrl("/")
                    .build());
        }

        Long savedBoardId = boardService.writeBoard(req, bcd, boardCategory, authentication.getName(), authentication);
        //log.info("auth.getname(): "+ authentication.getName());
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).get();
        UserRole userRole = user.getUserRole();

        String isExpert;

        if (userRole.equals((UserRole.EXPERT))){
            isExpert = "전문가입니다.";
        } else{
            isExpert = "일반유저입니다";
        }

        String message;
        String nextUrl;
        if (boardCategory.equals(BoardCategory.QUESTION)) {
            message = "질문글은 삭제할 수 없습니다.";
            nextUrl = "/";
        } else {
            message = savedBoardId + "번 글이 등록되었습니다.";
            nextUrl = "/boards/" + category + "/" + savedBoardId;
        }

        return ResponseEntity.ok(BoardWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .isExpert(isExpert)
                .userRole(userRole)
                .build());
    }


        @GetMapping("/read/{category}/{boardId}")
        @ResponseBody
        public BoardDto boardDetailPage(@PathVariable String category, @PathVariable Long boardId) {
            BoardDto boardDto = boardService.getBoard(boardId, category);
            if (boardDto == null) {
                // 게시글이 존재하지 않는 경우 null 반환 또는 적절한 에러 처리
                return null;
            }
            return boardDto;
        }


    @PostMapping("/{category}/{boardId}/edit")
    public ResponseEntity<BoardWriteResponse> boardEdit(@PathVariable String category, @PathVariable Long boardId,
                                                        @RequestBody BoardDto boardDto, BoardContentDto boardContentDto
                                                    ) throws IOException {


        Long editedBoardId = boardService.editBoard(boardId, category, boardDto);

        if (editedBoardId == null) {
            return ResponseEntity.badRequest().body(BoardWriteResponse.builder()
                    .message("게시글을 수정할 수 없습니다.")
                    .nextUrl("/")
                    .build());
        }

        String message;
        String nextUrl;

        BoardCategory boardCategory = BoardCategory.of(category);
        if (boardCategory == null) {
            return ResponseEntity.badRequest().body(BoardWriteResponse.builder()
                    .message("카테고리가 존재하지 않습니다.")
                    .nextUrl("/")
                    .build());
        }
        else {
            message = editedBoardId + "번 글이 수정되었습니다.";
            nextUrl = "/boards/" + category + "/" + editedBoardId;

            return ResponseEntity.ok(BoardWriteResponse.builder()
                    .message(message)
                    .nextUrl(nextUrl)
                    .build());
        }
    }

    @GetMapping("/{category}/{boardId}/delete")
    public ResponseEntity<BoardWriteResponse> boardDelete(@PathVariable String category, @PathVariable Long boardId) throws IOException {
        if (category.equals("greeting")) {
            return ResponseEntity.badRequest().body(BoardWriteResponse.builder()
                    .message("가입인사는 삭제할 수 없습니다.")
                    .nextUrl("/boards/greeting")
                    .build());
        }

        Long deletedBoardId = boardService.deleteBoard(boardId, category);

        String message;
        String nextUrl;
        if (deletedBoardId == null) {
            message = "해당 게시글이 존재하지 않습니다";
            nextUrl = "/boards/" + category;
        } else {
            message = deletedBoardId + "번 글이 삭제되었습니다.";
            nextUrl = "/boards/" + category;
        }

        return ResponseEntity.ok(BoardWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .build());
    }

}