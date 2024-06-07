package profit.login.question_board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import profit.login.entity.User;
import profit.login.entity.UserRole;
import profit.login.question_board.Entity.Board;
import profit.login.question_board.Entity.Reply;
import profit.login.question_board.dto.ReplyCreateRequest;
import profit.login.question_board.repository.BoardRepository;
import profit.login.question_board.repository.ReplyRepository;
import profit.login.repository.UserRepository;

import java.util.List;
import java.util.Optional;

//댓글 관련 CRUD
@Service
@RequiredArgsConstructor
@Slf4j
public class ReplyService {


    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public void writeReply(Long boardId, ReplyCreateRequest req, String email) {
        Board board = boardRepository.findById(boardId).get();
        User user = userRepository.findByEmail(email).get();
        replyRepository.save(req.toEntity(board, user));
    }

    public List<Reply> findAll(Long boardId) {
        return replyRepository.findAllByBoardId(boardId);
    }

    @Transactional
    public Long editReply(Long commentId, String newBody, String email) {
        Optional<Reply> optReply = replyRepository.findById(commentId);
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optReply.isEmpty() || optUser.isEmpty() || !optReply.get().getUser().equals(optUser.get())) {
            return null;
        }

        Reply reply = optReply.get();
        reply.update(newBody);

        return reply.getBoard().getId();
    }

    public Long deleteReply(Long replyId, String email) {
        Optional<Reply> optReply = replyRepository.findById(replyId);
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optReply.isEmpty() || optUser.isEmpty() ||
                (!optReply.get().getUser().equals(optUser.get()) && !optUser.get().getUserRole().equals(UserRole.NORMAL))) {
            return null;
        }

        Board board = optReply.get().getBoard();

        replyRepository.delete(optReply.get());
        return board.getId();
    }

    @Transactional
    public void selectReply(Long replyId, String email) {
        Optional<Reply> optReply = replyRepository.findById(replyId);
        if (optReply.isEmpty()) {
            throw new IllegalArgumentException("답글이 존재하지 않습니다.");
        }

        Reply reply = optReply.get();
        if (!reply.getBoard().getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("작성자만 답변을 채택할 수 있습니다.");
        }

        reply.select();
        User user = reply.getUser();
        log.info("user: " + user);
        user.addPoints(200);
        log.info("user points: " + user.getPoints());
        userRepository.save(user);
    }
}