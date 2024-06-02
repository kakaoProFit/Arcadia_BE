package profit.login.question_board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import profit.login.question_board.Entity.Board;
import profit.login.question_board.Entity.Like;
import profit.login.question_board.repository.BoardRepository;
import profit.login.question_board.repository.LikeRepository;
import profit.login.question_board.response.BoardListResponse;
import profit.login.question_board.dto.BoardSearchRequest;
import profit.login.question_board.service.BoardService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MyPageController {

    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final BoardService boardService;

    @GetMapping("/mypage/liked-boards/{userId}")
    public ResponseEntity<?> getLikedBoards(@PathVariable Long userId,
                                            @RequestParam(required = false, defaultValue = "1") int page,
                                            @RequestParam(required = false) String sortType,
                                            @RequestParam(required = false) String searchType,
                                            @RequestParam(required = false) String keyword) {
        // 유저가 좋아요를 누른 Like 엔티티 리스트를 가져옵니다.
        List<Like> likes = likeRepository.findAllByUserId(userId);

        // Like 엔티티에서 Board 엔티티를 추출합니다.
        List<Board> likedBoards = likes.stream()
                .map(Like::getBoard)
                .collect(Collectors.toList());

        // 페이지 요청을 설정합니다.
        PageRequest pageRequest = PageRequest.of(page - 1, 12, Sort.by("id").descending());
        if (sortType != null) {
            switch (sortType) {
                case "date":
                    pageRequest = PageRequest.of(page - 1, 12, Sort.by("createdAt").descending());
                    break;
                case "like":
                    pageRequest = PageRequest.of(page - 1, 12, Sort.by("likeCnt").descending());
                    break;
                case "comment":
                    pageRequest = PageRequest.of(page - 1, 12, Sort.by("commentCnt").descending());
                    break;
            }
        }

        // 검색 및 정렬 조건에 따라 좋아요한 게시물 목록을 페이지네이션합니다.
        Page<Board> pagedLikedBoards = boardService.getPagedBoards(likedBoards, pageRequest, searchType, keyword);
        log.info("Liked boards: " + pagedLikedBoards);

        // 검색 요청 정보를 설정합니다.
        BoardSearchRequest boardSearchRequest = new BoardSearchRequest(sortType, searchType, keyword);

        // 응답 객체를 생성합니다.
        BoardListResponse response = new BoardListResponse("liked", null, pagedLikedBoards, boardSearchRequest);

        return ResponseEntity.ok(response);
    }
}
