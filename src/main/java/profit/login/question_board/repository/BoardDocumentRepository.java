package profit.login.question_board.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import profit.login.question_board.dto.BoardContentDto;

@Repository
public interface BoardDocumentRepository extends MongoRepository<BoardContentDto, Long> {
}
