package wolox.training.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Optional;
import wolox.training.dtos.BookDto;
import wolox.training.models.Book;

public interface OpenLibraryService {

    Optional<BookDto> bookInfo(String isbn) ;
    Optional<Book> saveBookDto(BookDto bookDto);
}
