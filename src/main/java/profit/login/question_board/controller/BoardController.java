package profit.login.question_board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import profit.login.dto.LoginUserDto;
import profit.login.entity.User;
import profit.login.question_board.Entity.BoardCategory;
import profit.login.question_board.dto.BoardCreateRequest;
import profit.login.question_board.dto.BoardDto;
import profit.login.question_board.dto.BoardSearchRequest;
import profit.login.question_board.dto.CommentCreateRequest;
import profit.login.question_board.response.BoardWritePageResponse;
import profit.login.question_board.response.BoardWriteResponse;
import profit.login.question_board.service.BoardService;
import profit.login.question_board.service.UploadImageService;
import profit.login.question_board.service.LikeService;
import profit.login.question_board.service.CommentService;
import profit.login.service.AuthenticationService;

import java.io.IOException;
import java.net.MalformedURLException;

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

    @GetMapping("/{category}")
    public String boardListPage(@PathVariable String category, Model model,
                                @RequestParam(required = false, defaultValue = "1") int page,
                                @RequestParam(required = false) String sortType,
                                @RequestParam(required = false) String searchType,
                                @RequestParam(required = false) String keyword) {
        BoardCategory boardCategory = BoardCategory.of(category);
        if (boardCategory == null) {
            model.addAttribute("message", "카테고리가 존재하지 않습니다.");
            model.addAttribute("nextUrl", "/");
            return "printMessage";
        }

        model.addAttribute("notices", boardService.getNotice(boardCategory));

        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by("id").descending());
        if (sortType != null) {
            if (sortType.equals("date")) {
                pageRequest = PageRequest.of(page - 1, 10, Sort.by("createdAt").descending());
            } else if (sortType.equals("like")) {
                pageRequest = PageRequest.of(page - 1, 10, Sort.by("likeCnt").descending());
            } else if (sortType.equals("comment")) {
                pageRequest = PageRequest.of(page - 1, 10, Sort.by("commentCnt").descending());
            }
        }

        model.addAttribute("category", category);
        model.addAttribute("boards", boardService.getBoardList(boardCategory, pageRequest, searchType, keyword));
        model.addAttribute("boardSearchRequest", new BoardSearchRequest(sortType, searchType, keyword));
        return "boards/list";
    }

    @GetMapping("/{category}/write")
    public ResponseEntity<BoardWritePageResponse> boardWritePage(@PathVariable String category) {
        BoardCategory boardCategory = BoardCategory.of(category);
        // BoardCreateRequest를 사용하여 BoardWritePageResponse를 생성합니다.
        BoardCreateRequest boardCreateRequest = new BoardCreateRequest();
        if (boardCategory == null) {
            return ResponseEntity.badRequest()
                    .body(new BoardWritePageResponse("카테고리가 존재하지 않습니다.", boardCreateRequest));
        }



        // 생성된 BoardCreateRequest를 사용하여 BoardWritePageResponse를 생성합니다.
        BoardWritePageResponse response = new BoardWritePageResponse(category, boardCreateRequest);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/{category}")
    public ResponseEntity<BoardWriteResponse> boardWrite(@PathVariable String category, @ModelAttribute BoardCreateRequest req,
                                                         Authentication authentication) throws IOException {

        BoardCategory boardCategory = BoardCategory.of(category);
        if (boardCategory == null) {
            return ResponseEntity.badRequest().body(BoardWriteResponse.builder()
                    .message("카테고리가 존재하지 않습니다.")
                    .nextUrl("/")
                    .build());
        }

        Long savedBoardId = boardService.writeBoard(req, boardCategory, authentication.getName(), authentication);
        log.info("auth.getname(): "+ authentication.getName());

        String message;
        String nextUrl;
        if (boardCategory.equals(BoardCategory.GREETING)) {
            message = "가입인사를 작성하여 SILVER 등급으로 승급했습니다!\n이제 자유게시판에 글을 작성할 수 있습니다!";
            nextUrl = "/";
        } else {
            message = savedBoardId + "번 글이 등록되었습니다.";
            nextUrl = "/boards/" + category + "/" + savedBoardId;
        }

        return ResponseEntity.ok(BoardWriteResponse.builder()
                .message(message)
                .nextUrl(nextUrl)
                .build());
    }


    @GetMapping("/{category}/{boardId}")
    public String boardDetailPage(@PathVariable String category, @PathVariable Long boardId, Model model,
                                  Authentication authentication) {
        if (authentication != null) {
            model.addAttribute("loginUserLoginId", authentication.getName());
            model.addAttribute("likeCheck", likeService.checkLike(authentication.getName(), boardId));
        }

        BoardDto boardDto = boardService.getBoard(boardId, category);
        // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않는 경우
        if (boardDto == null) {
            model.addAttribute("message", "해당 게시글이 존재하지 않습니다");
            model.addAttribute("nextUrl", "/boards/" + category);
            return "printMessage";
        }

        model.addAttribute("boardDto", boardDto);
        model.addAttribute("category", category);

        model.addAttribute("commentCreateRequest", new CommentCreateRequest());
        model.addAttribute("commentList", commentService.findAll(boardId));
        return "boards/detail";
    }

    @PostMapping("/{category}/{boardId}/edit")
    public String boardEdit(@PathVariable String category, @PathVariable Long boardId,
                            @ModelAttribute BoardDto dto, Model model) throws IOException {
        Long editedBoardId = boardService.editBoard(boardId, category, dto);

        if (editedBoardId == null) {
            model.addAttribute("message", "해당 게시글이 존재하지 않습니다.");
            model.addAttribute("nextUrl", "/boards/" + category);
        } else {
            model.addAttribute("message", editedBoardId + "번 글이 수정되었습니다.");
            model.addAttribute("nextUrl", "/boards/" + category + "/" + boardId);
        }
        return "printMessage";
    }

    @GetMapping("/{category}/{boardId}/delete")
    public String boardDelete(@PathVariable String category, @PathVariable Long boardId, Model model) throws IOException {
        if (category.equals("greeting")) {
            model.addAttribute("message", "가입인사는 삭제할 수 없습니다.");
            model.addAttribute("nextUrl", "/boards/greeting");
            return "printMessage";
        }

        Long deletedBoardId = boardService.deleteBoard(boardId, category);

        // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않으면 에러 메세지 출력
        // 게시글이 존재해 삭제했으면 삭제 완료 메세지 출력
        model.addAttribute("message", deletedBoardId == null ? "해당 게시글이 존재하지 않습니다" : deletedBoardId + "번 글이 삭제되었습니다.");
        model.addAttribute("nextUrl", "/boards/" + category);
        return "printMessage";
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource showImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + uploadImageService.getFullPath(filename));
    }

    @GetMapping("/images/download/{boardId}")
    public ResponseEntity<UrlResource> downloadImage(@PathVariable Long boardId) throws MalformedURLException {
        return uploadImageService.downloadImage(boardId);
    }
}