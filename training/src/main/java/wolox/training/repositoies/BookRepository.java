package wolox.training.repositoies;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findFirstByAuthor(String author);
}
