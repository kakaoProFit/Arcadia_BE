package profit.login.question_board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import profit.login.question_board.Entity.UploadImage;

@Repository
public interface UploadImageRepository extends JpaRepository<UploadImage, Long> {
}