package profit.login.question_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import profit.login.question_board.Entity.Reply;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByBoardId(Long boardId);
    List<Reply> findAllByUserEmail(String email);

}