package wolox.training.repositoies;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findFirstByName(String name);

    List<User> findByBirthdateBetweenAndNameContainingIgnoreCase(LocalDate startDate,
                                                        LocalDate endDate,
                                                        String name);

}
