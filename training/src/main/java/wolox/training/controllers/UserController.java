package wolox.training.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositoies.BookRepository;
import wolox.training.repositoies.UserRepository;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    /**
     *
     * This method return {@link List} with all instanced users
     *
     * @return {@link List}
     * @throws UserNotFoundException : rows exception if user was not found
     */
    @GetMapping
    public List<User> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        if(users.isEmpty()){
            throw new UserNotFoundException();
        }else {
            return users;
        }
    }

    /**
     *
     * This method returns User instance filtered for ID
     *
     * @param id :id of user to search
     * @return {@link User}
     * @throws UserNotFoundException : trows exception if the user was not found
     *
     */
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User findById(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Id:" + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        if (user.getId() != id) {
            throw new UserIdMismatchException("User id: "+user.getId()+" don't match with Id:"+id);
        }
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Id:" + id + " not found"));
        return userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.findById(id)
                .orElseThrow(()-> new BookNotFoundException("User Id:"+id+" not found"));
        userRepository.deleteById(id);
    }

    /**
     *
     * This method adds a book in user collections
     *
     * @param book :Object book to add
     * @param userId: user id to update
     * @throws BookNotFoundException : throw this exception if book not found
     * @throws UserNotFoundException: throw this exception if User not found
     */
    @PatchMapping(path = "/{userID}/books/add")
    public void addBookinUserCollection(@RequestBody Book book, @PathVariable Long userId){
        Book finalBook = bookRepository.findById(book.getId())
                .orElseThrow(()->new BookNotFoundException("Book Id:"+ book.getId()+" not found"));
        userRepository.findById(userId)
                .orElseThrow(()-> new BookNotFoundException("User Id:"+userId+" not found"))
                .addBookToCollection(finalBook);
    }

    /**
     *
     * This Method removes a book of user collection
     *
     * @param book : book to remove
     * @param userId : userId
     * @throws BookNotFoundException : throw this exception if book not found
     * @throws UserNotFoundException: throw this exception if User not found
     */
    @PatchMapping(path = "/{userID}/books/delete")
    public void removeBookinUserCollection(@RequestBody Book book, @PathVariable Long userId){
        Book finalBook = bookRepository.findById(book.getId())
                .orElseThrow(()->new BookNotFoundException("Book Id:"+ book.getId()+" not found"));
        userRepository.findById(userId)
                .orElseThrow(()-> new BookNotFoundException("User Id:"+userId+" not found"))
                .removeBookToCollection(finalBook);
    }
}
