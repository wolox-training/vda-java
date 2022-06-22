package wolox.training.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Actor Not Found")
public class BookAlreadyOwnedException extends RuntimeException{

    public BookAlreadyOwnedException() {
        super();
    }

    public BookAlreadyOwnedException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookAlreadyOwnedException(Throwable cause) {
        super(cause);
    }

    public BookAlreadyOwnedException(String message) {
        super(message);
    }
}
