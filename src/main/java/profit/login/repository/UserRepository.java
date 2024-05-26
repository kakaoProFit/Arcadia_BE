package profit.login.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import profit.login.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findById(String id);

}