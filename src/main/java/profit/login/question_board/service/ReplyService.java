package profit.login.question_board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import profit.login.question_board.Entity.Comment;
import profit.login.entity.User;
import profit.login.entity.UserRole;
import profit.login.question_board.Entity.Board;
import profit.login.question_board.Entity.Reply;
import profit.login.question_board.dto.CommentCreateRequest;
import profit.login.question_board.dto.ReplyCreateRequest;
import profit.login.question_board.repository.BoardRepository;
import profit.login.question_board.repository.CommentRepository;
import profit.login.question_board.repository.ReplyRepository;
import profit.login.repository.UserRepository;

import java.util.List;
import java.util.Optional;

//댓글 관련 CRUD
@Service
@RequiredArgsConstructor
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
    public Long editComment(Long commentId, String newBody, String email) {
        Optional<Reply> optReply = replyRepository.findById(commentId);
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optReply.isEmpty() || optUser.isEmpty() || !optReply.get().getUser().equals(optUser.get())) {
            return null;
        }

        Reply reply = optReply.get();
        reply.update(newBody);

        return reply.getBoard().getId();
    }

    public Long deleteComment(Long replyId, String email) {
        Optional<Reply> optReply = replyRepository.findById(replyId);
        Optional<User> optUser = userRepository.findByEmail(email);
        if (optReply.isEmpty() || optUser.isEmpty() ||
                (!optReply.get().getUser().equals(optUser.get()) && !optUser.get().getUserRole().equals(UserRole.ADMIN))) {
            return null;
        }

        Board board = optReply.get().getBoard();

        replyRepository.delete(optReply.get());
        return board.getId();
    }
}