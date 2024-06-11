package profit.login.question_board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import profit.login.entity.User;
import profit.login.entity.UserRole;
import profit.login.question_board.Entity.Board;
import profit.login.question_board.Entity.Comment;
import profit.login.question_board.Entity.Reply;
import profit.login.question_board.dto.BoardContentDto;
import profit.login.question_board.dto.ReplyCreateRequest;
import profit.login.question_board.repository.BoardDocumentRepository;
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
    private final BoardDocumentRepository boardDocumentRepository;

    public void writeReply(Long boardId, ReplyCreateRequest req, String email) {
        Board board = boardRepository.findById(boardId).get();
        User user = userRepository.findByEmail(email).get();
        Reply reply = replyRepository.save(req.toEntity(board, user, user.getNickname()));

        BoardContentDto bcd = new BoardContentDto();
        bcd = boardDocumentRepository.save(bcd.init(req.getBody())); // MongoDB에 저장하고 반환된 bcd로 업데이트
        reply.setDocumentId(bcd.getId());
        replyRepository.save(reply);


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

        Reply reply = optReply.get();
        Board board = optReply.get().getBoard();

        replyRepository.delete(optReply.get());
        boardDocumentRepository.deleteById(reply.getDocumentId());
        return board.getId();
    }

    public List<Reply> getReplyByBoardId(Long boardId) {
        return replyRepository.findAllByBoardId(boardId);
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

        Integer customPoints = reply.getBoard().getPoint();



        reply.select();
        User user = reply.getUser();
        log.info("user: " + user);
        user.addPoints(customPoints);
        log.info("user points: " + user.getPoints());
        userRepository.save(user);
    }
}