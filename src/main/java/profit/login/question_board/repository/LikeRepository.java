package profit.login.question_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import profit.login.question_board.Entity.Like;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    void deleteByUserEmailAndBoardId(String email, Long boardId);
    Boolean existsByUserEmailAndBoardId(String email, Long boardId);
    List<Like> findAllByUserId(Long userId);
}
