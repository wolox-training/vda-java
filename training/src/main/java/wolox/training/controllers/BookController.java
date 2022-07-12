package wolox.training.controllers;


import io.swagger.annotations.Api;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.mappers.ModelSpecificationMapper;
import wolox.training.dtos.BookDto;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositoies.BookRepository;
import wolox.training.services.OpenLibraryService;

@RestController
@RequestMapping("/api/books")
@Api
public class BookController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private ModelSpecificationMapper modelMapper;

    @Autowired
    private OpenLibraryService openLibraryService;

    /**
     *
     * This method permits greeting to a user for test
     *
     * @param name :Name of user
     * @param model : {@link Model} Object
     * @return Model with attributeName to will show
     */
    @GetMapping("/greeting")
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "World") String name,
            Model model) {
        model.addAttribute("name", name);
        return "greeting";
    }

    /**
     *
     * This method returns all Book in repository whit optional params for : genre, author,
     * title, subtitle, publisher, year, pages.
     *
     * @return {@link List} with all instances of {@link Book} filters by params sent.
     * @throws BookNotFoundException :trows exception if the book was not found
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<Book> findAllWithFilters(
            @RequestParam (required = false) Optional<String> genre,
            @RequestParam (required = false) Optional<String> author,
            @RequestParam (required = false) Optional<String> title,
            @RequestParam (required = false) Optional<String> subtitle,
            @RequestParam (required = false) Optional<String> publisher,
            @RequestParam (required = false) Optional<String> year,
            @RequestParam (required = false) Optional<Integer> pages,
            @PageableDefault(value = 3, page = 0) Pageable pageable
            ) {
        Page<Book> books = bookRepository.findBooksWithOptionalFilters(
                genre.orElse(null),
                author.orElse(null),
                title.orElse(null),
                subtitle.orElse(null),
                publisher.orElse(null),
                year.orElse(null),
                pages.orElse(null),
                pageable
        );
        if(books.isEmpty()){
            throw new BookNotFoundException();
        }else {
            return books;
        }
    }

    /**
     *
     * This method returns Book instance filtered for ID
     *
     * @param id :id of book to search
     * @return {@link Book}
     * @throws BookNotFoundException : trows exception if the book was not found
     *
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book findById(@PathVariable Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book Id:" + id + " not found"));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @PutMapping(value = "/{id}",
            produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        if (book.getId() != id) {
            throw new BookIdMismatchException("Book id: "+book.getId()+" don't match with Id:"+id);
        }
        bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book Id:" + id + " not found"));
        return bookRepository.save(book);
    }
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id)
                .orElseThrow(()-> new BookNotFoundException("Book Id:"+id+" not found"));
        bookRepository.deleteById(id);
    }
    /**
     *
     * This method returns {@link  List<Book>} filtered for isbn
     *
     * @param isbn :Book's isbn to search
     * @return {@link  Book} and it creates book on local database when the
     * book is not found the local database but is in openlibrary.
     * @throws BookNotFoundException : trows exception if the book was not found
     *
     */
    @GetMapping(params = "isbn")
    public ResponseEntity<Book> findByIsbn(@RequestParam String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElse(null);
        if(book == null){
            try {
                    BookDto bookDto = openLibraryService.bookInfo(isbn)
                            .orElseThrow(()->new BookNotFoundException("Book isbn:"+isbn+"not found"));
                    book = openLibraryService.saveBookDto(bookDto).orElseThrow(RuntimeException::new);
                    return ResponseEntity.status(HttpStatus.CREATED).body(book);
                } catch (Exception e) { throw new RuntimeException(e); }
        }
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

}
