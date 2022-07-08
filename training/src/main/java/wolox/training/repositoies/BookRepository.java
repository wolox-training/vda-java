package wolox.training.repositoies;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findFirstByAuthor(String author);
    List<Book> findByTitle(String title);
    Optional<Book> findByIsbn(String isbn);
    @Query("select b from books b where "
            + "(:publisher is null or  b.publisher = :publisher)"
            + "and (:genre is null or b.genre = :genre) "
            + "and (:year is null or b.year = :year)"
            )
    List<Book> findByPublisherAndGenreAndYear(@Param("publisher") String publisher,
                                                @Param("genre") String genre,
                                                @Param("year") String year);
}
