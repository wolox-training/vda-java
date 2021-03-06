package wolox.training.repositoies;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.User;


public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findFirstByName(String name);

}
