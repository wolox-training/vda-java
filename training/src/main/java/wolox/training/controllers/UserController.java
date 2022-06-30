package wolox.training.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
@Api
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

    @ApiOperation(value = "Return List with users instances", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Search"),
            @ApiResponse(code = 400, message = "User not found")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
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
    @ApiOperation(value = "Return user filtered for id", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully Search"),
            @ApiResponse(code = 400, message = "User not found")
    })
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User findById(
            @ApiParam(value = "User id to find", required = true)
            @PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Id:" + id + " not found"));
    }
    @ApiOperation(value = "Create New User", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User created Successfully"),
            @ApiResponse(code = 400, message = "User not found")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(
            @ApiParam(value = "User properties in body", required = true)
            @RequestBody User user) {
        return userRepository.save(user);
    }
    @ApiOperation(value = "Update User Params", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated Successfully"),
            @ApiResponse(code = 400, message = "User not found"),
            @ApiResponse(code = 400, message = "User not match whit Id")
    })
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(
            @ApiParam(value = "User Entity with new data", required = true)
            @RequestBody User user,
            @ApiParam(value = "User id to update", required = true)
            @PathVariable Long id) {
        if (user.getId() != id) {
            throw new UserIdMismatchException("User id: "+user.getId()+" don't match with Id:"+id);
        }
        userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Id:" + id + " not found"));
        return userRepository.save(user);
    }
    @ApiOperation(value = "Delete User", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User updated Successfully"),
            @ApiResponse(code = 400, message = "User not found"),
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @ApiParam(value = "User Id to delete", required = true)
            @PathVariable Long id) {
        userRepository.findById(id)
                .orElseThrow(()-> new UserIdMismatchException("User Id:"+id+" not found"));
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
    @ApiOperation(value = "Add Book to User library collection", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User Collection updated Successfully"),
            @ApiResponse(code = 400, message = "User not found"),
            @ApiResponse(code = 400, message = "Book not found"),
            })
    @PostMapping(path = "/{userId}/books")
    @ResponseStatus(HttpStatus.OK)
    public void addBookinUserCollection(
            @ApiParam(value = "New Book Entity", required = true)
            @RequestBody Book book,
            @ApiParam(value = "User id to update", required = true)
            @PathVariable Long userId){
        Book finalBook = bookRepository.findById(book.getId())
                .orElseThrow(()->new BookNotFoundException("Book Id:"+ book.getId()+" not found"));
        userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User Id:"+userId+" not found"))
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
    @ApiOperation(value = "Delete Book to User library collection", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User Collection updated Successfully"),
            @ApiResponse(code = 400, message = "User not found"),
            @ApiResponse(code = 400, message = "Book not found"),
    })
    @DeleteMapping(path = "/{userId}/books")
    @ResponseStatus(HttpStatus.OK)
    public void removeBookinUserCollection(
            @ApiParam(value = "New Book Entity", required = true)
            @RequestBody Book book,
            @ApiParam(value = "User id to update", required = true)
            @PathVariable Long userId){

        Book finalBook = bookRepository.findById(book.getId())
                .orElseThrow(()->new BookNotFoundException("Book Id:"+ book.getId()+" not found"));
        userRepository.findById(userId)
                .orElseThrow(()-> new UserNotFoundException("User Id:"+userId+" not found"))
                .removeBookToCollection(finalBook);
    }
}
