package profit.login.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import profit.login.dto.MongoDBTestModel;

public interface MongoDBTestRepository extends MongoRepository<MongoDBTestModel, String> {
    MongoDBTestModel findByName(String name);
}
