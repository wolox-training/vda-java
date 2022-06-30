package wolox.training.controllers;

import io.swagger.annotations.Api;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositoies.BookRepository;

@RestController
@RequestMapping("/api/books")
@Api
public class BookController {

    @Autowired
    private BookRepository bookRepository;

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
     * This method returns all Book in repository
     *
     * @return {@link List} with all instances of {@link Book}
     * @throws BookNotFoundException :trows exception if the book was not found
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> findAll() {
        List<Book> books = (List<Book>) bookRepository.findAll();
        if(books.isEmpty()){
            throw new BookNotFoundException();
        }else {
            return books;
        }
    }

    /**
     *
     * This method returns a list with books filtered for name
     *
     * @param title : title to search
     * @return {@link List} of {@link Book} filtered for title
     * @throws BookNotFoundException: trows exception if the book was not found
     *
     */
    @GetMapping(params = "title")
    @ResponseStatus(HttpStatus.OK)
    public List<Book> findByTitle(@RequestParam String title) {
        List<Book> books = bookRepository.findByTitle(bookTitle);
        if (books.isEmpty()){
            throw new BookNotFoundException();
        }else{
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id)
                .orElseThrow(()-> new BookNotFoundException("Book Id:"+id+" not found"));
        bookRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException("Book id: "+book.getId()+" don't match with Id:"+id);
        }
        bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book Id:" + id + " not found"));
        return bookRepository.save(book);
    }

}
