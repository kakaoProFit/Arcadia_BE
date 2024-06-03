package profit.login.question_board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import profit.login.question_board.Entity.Like;
import profit.login.entity.User;
import profit.login.question_board.Entity.Board;
import profit.login.question_board.Entity.Reply;
import profit.login.question_board.repository.BoardRepository;
import profit.login.question_board.repository.LikeRepository;
import profit.login.question_board.repository.ReplyRepository;
import profit.login.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;

    @Transactional
    public void addLike(String email, Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        User loginUser = userRepository.findByEmail(email).get();
        User boardUser = board.getUser();

        // 자신이 누른 좋아요가 아니라면
        if (!boardUser.equals(loginUser)) {
            boardUser.likeChange(boardUser.getReceivedLikeCnt() + 1);
        }
        board.likeChange(board.getLikeCnt() + 1);

        likeRepository.save(Like.builder()
                .user(loginUser)
                .board(board)
                .build());
    }
    @Transactional
    public void addLikeToReply(String email, Long replyId) {
        Reply reply = replyRepository.findById(replyId).get();
        User loginUser = userRepository.findByEmail(email).get();
        User boardUser = reply.getUser();

        // 자신이 누른 좋아요가 아니라면
        if (!boardUser.equals(loginUser)) {
            boardUser.likeChange(boardUser.getReceivedLikeCnt() + 1);
        }
        reply.likeChange(reply.getLikeCnt() + 1);

        likeRepository.save(Like.builder()
                .user(loginUser)
                .reply(reply)
                .build());
    }

    @Transactional
    public void deleteLike(String email, Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        User loginUser = userRepository.findByEmail(email).get();
        User boardUser = board.getUser();

        // 자신이 누른 좋아요가 아니라면
        if (!boardUser.equals(loginUser)) {
            boardUser.likeChange(boardUser.getReceivedLikeCnt() - 1);
        }
        board.likeChange(board.getLikeCnt() - 1);

        likeRepository.deleteByUserEmailAndBoardId(email, boardId);
    }

    public Boolean checkLike(String email, Long boardId) {
        return likeRepository.existsByUserEmailAndBoardId(email, boardId);
    }
}