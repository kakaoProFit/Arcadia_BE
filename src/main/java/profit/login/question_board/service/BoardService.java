package profit.login.question_board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import profit.login.entity.Comment;
import profit.login.entity.Like;
import profit.login.entity.User;
import profit.login.entity.UserRole;
import profit.login.question_board.Entity.Board;
import profit.login.question_board.Entity.BoardCategory;
import profit.login.question_board.Entity.UploadImage;
import profit.login.question_board.dto.BoardCntDto;
import profit.login.question_board.dto.BoardContentDto;
import profit.login.question_board.dto.BoardCreateRequest;
import profit.login.question_board.dto.BoardDto;
import profit.login.question_board.repository.BoardDocumentRepository;
import profit.login.question_board.repository.BoardRepository;
import profit.login.question_board.repository.CommentRepository;
import profit.login.question_board.repository.LikeRepository;
import profit.login.repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UploadImageService uploadImageService;

    private final BoardDocumentRepository boardDocumentRepository;

        public Page<Board> getBoardList(BoardCategory category, PageRequest pageRequest, String searchType, String keyword) {
            if (searchType != null && keyword != null) {
                if (searchType.equals("title")) {
                    log.info("find all: " + boardRepository.findAllByCategoryAndTitleContainsAndUserUserRoleNot(category, keyword, UserRole.ADMIN, pageRequest));
                    return boardRepository.findAllByCategoryAndTitleContainsAndUserUserRoleNot(category, keyword, UserRole.ADMIN, pageRequest);
                }
                else {
                    return boardRepository.findAllByCategoryAndUserNicknameContainsAndUserUserRoleNot(category, keyword, UserRole.ADMIN, pageRequest);
                }
            }
            return boardRepository.findAllByCategoryAndUserUserRoleNot(category, UserRole.ADMIN, pageRequest);
        }

    public List<Board> getNotice(BoardCategory category) {
        return boardRepository.findAllByCategoryAndUserUserRole(category, UserRole.ADMIN);
    }

    public BoardDto getBoard(Long boardId, String category) {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않으면 null return
        if (optBoard.isEmpty() || !optBoard.get().getCategory().toString().equalsIgnoreCase(category)) {
            return null;
        }

        return BoardDto.of(optBoard.get());
    }

    @Transactional
    public Long writeBoard(BoardCreateRequest req, BoardContentDto bcd, BoardCategory category, String email, Authentication authentication) throws IOException {

        User loginUser = userRepository.findByEmail(email).get();

        Board savedBoard = boardRepository.save(req.toEntity(category, loginUser));
        BoardContentDto savedBoardDocument = boardDocumentRepository.save(bcd.init(savedBoard.getId(),savedBoard.getBody()));

//        UploadImage uploadImage = uploadImageService.saveImage(req.getUploadImage(), savedBoard);
//        if (uploadImage != null) {
//            savedBoard.setUploadImage(uploadImage);
//        }
//         아래 부분에 글작성시 추가 포인트 지급하는 코드 구현 필요
        return savedBoard.getId();
    }

    @Transactional
    public Long editBoard(Long boardId, String category, BoardDto dto) throws IOException {
        Optional<Board> optBoard = boardRepository.findById(boardId);
        Optional<BoardContentDto> optBoard2 = boardDocumentRepository.findById(boardId);

        System.out.println(boardId);

        // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않으면 null return
        if (optBoard.isEmpty() || optBoard2.isEmpty() || !optBoard.get().getCategory().toString().equalsIgnoreCase(category)) {
            return null;
        }

        Board board = optBoard.get();
        BoardContentDto boardContentDto = optBoard2.get();

//        System.out.println(boardContentDto.getContent());
//        System.out.println(dto.getBody());
        board.update(dto);
        boardContentDto.update(dto.getBody());
//        System.out.println(boardContentDto.getContent());

        return board.getId();
    }
//   질문글은 삭제 불가.


    public Long deleteBoard(Long boardId, String category) throws IOException {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않으면 null return
        if (optBoard.isEmpty() || !optBoard.get().getCategory().toString().equalsIgnoreCase(category)) {
            return null;
        }
        //질문게시글은 어차피 삭제 안되고, 자유 게시글을 삭제할 경우 해당 게시글에 좋아를요를 표시한 유저의 좋아요 표시글 리스트를 갱신한다.
//        User boardUser = optBoard.get().getUser();
//        boardUser.likeChange(boardUser.getReceivedLikeCnt() - optBoard.get().getLikeCnt());
//        if (optBoard.get().getUser() != null) {
//            uploadImageService.deleteImage(optBoard.get().getUploadImage());
//        }
        boardRepository.deleteById(boardId);
        boardDocumentRepository.deleteById(boardId);
        return boardId;
    }

    public String getCategory(Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        return board.getCategory().toString().toLowerCase();
    }

    public List<Board> findMyBoard(String category, String email) {
        if (category.equals("board")) {
            return boardRepository.findAllByUserEmail(email);
        } else if (category.equals("like")) {
            List<Like> likes = likeRepository.findAllByUserEmail(email);
            List<Board> boards = new ArrayList<>();
            for (Like like : likes) {
                boards.add(like.getBoard());
            }
            return boards;
        } else if (category.equals("comment")) {
            List<Comment> comments = commentRepository.findAllByUserEmail(email);
            List<Board> boards = new ArrayList<>();
            //HashSet을 사용하여 중복을 제거해서 return
            HashSet<Long> commentIds = new HashSet<>();

            for (Comment comment : comments) {
                if (!commentIds.contains(comment.getBoard().getId())) {
                    boards.add(comment.getBoard());
                    commentIds.add(comment.getBoard().getId());
                }
            }
            return boards;
        }
        return null;
    }
    //Admin 있을때 아래사용
    public BoardCntDto getBoardCnt(){
        return BoardCntDto.builder()
                .totalBoardCnt(boardRepository.count())
                .totalNoticeCnt(boardRepository.countAllByUserUserRole(UserRole.ADMIN))
                .totalGreetingCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.GREETING, UserRole.ADMIN))
                .totalFreeCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.FREE, UserRole.ADMIN))
                .totalGoldCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.GOLD, UserRole.ADMIN))
                .build();
    }
}