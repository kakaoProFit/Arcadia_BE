package profit.login.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import profit.login.login_rest_api_security_jwt.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByEmail(String email);
}