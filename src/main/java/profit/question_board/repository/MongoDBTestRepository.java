package profit.question_board.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import profit.question_board.Entity.Board;


public interface MongoDBTestRepository extends MongoRepository<Board, String> {
    Board findByTitle(String title);

}
