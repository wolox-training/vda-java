package wolox.training.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Book already Owned in user's collection")
public class BookAlreadyOwnedException extends RuntimeException {

    public BookAlreadyOwnedException() {
        super("Book already Owned in user's collection");
    }

}
