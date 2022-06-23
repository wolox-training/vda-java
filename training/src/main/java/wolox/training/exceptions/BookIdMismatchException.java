package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Book Not Found")
public class BookIdMismatchException extends RuntimeException {

    public BookIdMismatchException() {

        super();
    }
    public BookIdMismatchException(String message, Throwable cause) {

        super(message, cause);
    }
    public BookIdMismatchException(String message) {

        super(message);
    }
    public BookIdMismatchException(Throwable cause) {

        super(cause);
    }
}
