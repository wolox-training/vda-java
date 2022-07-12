package wolox.training.repositoies;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wolox.training.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {
    Optional<Book> findFirstByAuthor(String author);

    Optional<Book> findByIsbn(String isbn);
    @Query("select b from books b where "
            + "(:genre is null or  b.genre = :genre)"
            + "and (:author is null or b.author = :author) "
            + "and (:title is null or b.title = :title)"
            + "and (:subtitle is null or b.subtitle = :subtitle)"
            + "and (:publisher is null or b.publisher = :publisher)"
            + "and (:year is null or b.year = :year)"
            + "and (:pages is null or b.pages = :pages)"
    )
    Page<Book> findBooksWithOptionalFilters(
            @Param("genre") String genre,
            @Param("author") String author,
            @Param("title") String title,
            @Param("subtitle") String subtitle,
            @Param("publisher") String publisher,
            @Param("year") String year,
            @Param("pages") Integer pages,
            Pageable pageable
            );
}
