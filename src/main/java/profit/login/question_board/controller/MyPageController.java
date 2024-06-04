package profit.login.question_board.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import profit.login.question_board.Entity.Like;
import profit.login.question_board.dto.BoardDto;
import profit.login.question_board.response.LikedBoardListResponse;
import profit.login.question_board.service.BoardService;
import profit.login.question_board.repository.LikeRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/mypage")
public class MyPageController {

    private final LikeRepository likeRepository;
    private final BoardService boardService;

    @GetMapping("/liked-boards/{userId}")
    public ResponseEntity<?> getLikedBoards(@PathVariable Long userId) {
        // 유저가 좋아요를 누른 Like 엔티티 리스트를 가져옵니다.
        List<Like> likes = likeRepository.findAllByUserId(userId);
        log.info("likes: " + likes);

        // Like 엔티티에서 Board 엔티티를 추출하고 DTO로 변환합니다.
        List<BoardDto> likedBoards = likes.stream()
                .map(Like::getBoard)
                .map(BoardDto::of)
                .collect(Collectors.toList());
        log.info("List Board: " + likedBoards);

        // 응답 객체를 생성합니다.
        LikedBoardListResponse response = new LikedBoardListResponse("liked board list", likedBoards);

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/writed-boards/{userId}")
//    public ResponseEntity<?> getWritedBoards(@PathVariable Long userId){
//        List
//    }
}
