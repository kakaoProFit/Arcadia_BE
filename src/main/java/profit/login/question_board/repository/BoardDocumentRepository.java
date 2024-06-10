package profit.login.question_board.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import profit.login.question_board.Entity.Board;
import profit.login.question_board.dto.BoardContentDto;

import java.util.Optional;

@Repository
public interface BoardDocumentRepository extends MongoRepository<BoardContentDto, String> {
//    Optional<BoardContentDto> findByDocumentId(ObjectId documentId);
}
