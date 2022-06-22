package wolox.training.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.repositoies.BookRepository;

@Controller
@RequestMapping("/books")
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
     * @param bookTitle :Title to search
     * @return {@link List} of {@link Book} filtered for title
     * @throws BookNotFoundException: trows exception if the book was not found
     *
     */
    @GetMapping("/title/{bookTitle}")
    @ResponseStatus(HttpStatus.OK)
    public List<Book> findByTitle(@PathVariable String bookTitle) {
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
                .orElseThrow(BookNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }


}
