package wolox.training.repositoies;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findFirstByAuthor(String author);
    List<Book> findByTitle(String title);
    Optional<Book> findByIsbn(String isbn);
}
