package profit.login.question_board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import profit.login.entity.User;
import profit.login.entity.UserRole;
import profit.login.question_board.Entity.Board;
import profit.login.question_board.Entity.BoardCategory;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;


    private final BoardDocumentRepository boardDocumentRepository;

        public Page<Board> getBoardList(BoardCategory category, PageRequest pageRequest, String searchType, String keyword) {
            if (searchType != null && keyword != null) {
                if (searchType.equals("title")) {
                    return boardRepository.findAllByCategoryAndTitleContains(category, keyword, pageRequest);
                }
                else if (searchType.equals("author")) {
                    List<User> userOptional = userRepository.findByFullNameContains(keyword);
                    return boardRepository.findAllByCategoryAndUserIn(category, userOptional, pageRequest);
                }
            }
            return boardRepository.findAllByCategoryAndUserUserRoleNot(category, UserRole.NORMAL, pageRequest);
        }


    public BoardDto getBoard(Long boardId, String category) {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않으면 null return
        if (optBoard.isEmpty()) {
            return null;
        }

        Board board = optBoard.get();
        board.incrementViewCount(); // 조회수 증가
        boardRepository.save(board); // 변경 사항 저장

        BoardDto boardDto = BoardDto.of(optBoard.get());

        Optional<BoardContentDto> savedBoardDocument = boardDocumentRepository.findById(board.getDocumentId());

        if (savedBoardDocument.isEmpty()) {
            return null;
        }

        BoardContentDto boardContentDto = savedBoardDocument.get();
        boardDto.setBody(boardContentDto.getBody());

        return boardDto;
    }

    @Transactional
    public Long writeBoard(BoardCreateRequest req, BoardCategory category, String email, Authentication authentication) throws IOException {

        User loginUser = userRepository.findByEmail(email).get();
        Board savedBoard = boardRepository.save(req.toEntity(category, loginUser));

        // BoardContentDto 엔티티 생성 및 초기화
        BoardContentDto bcd = new BoardContentDto();
        bcd = boardDocumentRepository.save(bcd.init(req.getBody())); // MongoDB에 저장하고 반환된 bcd로 업데이트

        // 저장된 BoardContentDto의 ObjectId를 가져와 BoardCreateRequest의 documentId에 설정
        savedBoard.setDocumentId(bcd.getId());
        boardRepository.save(savedBoard);

//         아래 부분에 글작성시 추가 포인트 지급하는 코드 구현 필요
        return savedBoard.getId();
    }

    @Transactional
    public Long editBoard(Long boardId, String category, BoardDto dto) throws IOException {

        Optional<Board> savedBoard = boardRepository.findById(boardId);

        if (savedBoard.isEmpty()) {
            return null;
        }

        Board board = savedBoard.get();

        Optional<BoardContentDto> savedBoardDocument = boardDocumentRepository.findById(board.getDocumentId());

        if (savedBoardDocument.isEmpty()) {
            return null;
        }


        BoardContentDto boardContentDto = savedBoardDocument.get();

        board.update(dto);
        boardContentDto.update(dto.getBody());

        boardRepository.save(board);
        boardDocumentRepository.save(boardContentDto);

        return board.getId();
    }
//   질문글은 삭제 불가.


    public Long deleteBoard(Long boardId, String category) throws IOException {
        Optional<Board> optBoard = boardRepository.findById(boardId);

        // id에 해당하는 게시글이 없거나 카테고리가 일치하지 않으면 null return
        if (optBoard.isEmpty() || !optBoard.get().getCategory().toString().equalsIgnoreCase(category)) {
            return null;
        }

        Board board = optBoard.get();

        //질문게시글은 어차피 삭제 안되고, 자유 게시글을 삭제할 경우 해당 게시글에 좋아를요를 표시한 유저의 좋아요 표시글 리스트를 갱신한다.
//        User boardUser = optBoard.get().getUser();
//        boardUser.likeChange(boardUser.getReceivedLikeCnt() - optBoard.get().getLikeCnt());
//        if (optBoard.get().getUser() != null) {
//            uploadImageService.deleteImage(optBoard.get().getUploadImage());
//        }
        boardRepository.deleteById(boardId);
        boardDocumentRepository.deleteById(board.getDocumentId());
        return boardId;
    }

    public String getCategory(Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        return board.getCategory().toString().toLowerCase();
    }




    //Admin 있을때 아래사용
    public BoardCntDto getBoardCnt(){
        return BoardCntDto.builder()
                .totalBoardCnt(boardRepository.count())
                .totalNoticeCnt(boardRepository.countAllByUserUserRole(UserRole.NORMAL))
                .totalQuestionCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.QUESTION, UserRole.NORMAL))
                .totalFreeCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.FREE, UserRole.NORMAL))
                .totalInformCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.INFORM, UserRole.NORMAL))
                .totalDiaryCnt(boardRepository.countAllByCategoryAndUserUserRoleNot(BoardCategory.DIARY, UserRole.NORMAL))
                .build();
    }

    public Page<Board> getPagedBoards(List<Board> boards, PageRequest pageRequest, String searchType, String keyword) {
        // 검색 및 필터링 로직을 추가할 수 있습니다.
        List<Board> filteredBoards = boards.stream()
                .filter(board -> {
                    if (searchType != null && keyword != null) {
                        switch (searchType) {
                            case "title":
                                return board.getTitle().contains(keyword);
                            case "body":
                                return board.getBody().contains(keyword);
                            // 필요한 경우 다른 검색 유형을 추가할 수 있습니다.
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());

        // 필터링된 게시물 목록을 페이지네이션합니다.
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), filteredBoards.size());
        return new PageImpl<>(filteredBoards.subList(start, end), pageRequest, filteredBoards.size());
    }
}