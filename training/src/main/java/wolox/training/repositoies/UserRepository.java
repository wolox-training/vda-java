package wolox.training.repositoies;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Page<User> findAll(Pageable pageable);
    Optional<User> findFirstByName(String name);

    @Query("select u from users u where "
            + "u.birthdate between :startDate and :endDate "
            + "and (:name is null or upper(u.name) like upper(concat('%', :name, '%')))"
    )
    Page<User> findByBirthdateBetweenAndNameContainingIgnoreCase(@Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate,
                                                        @Param("name") String name,
                                                        Pageable pageable
                                                        );

}
