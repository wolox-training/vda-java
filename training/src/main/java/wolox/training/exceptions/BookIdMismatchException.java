package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Id mismatch with Book id")
public class BookIdMismatchException extends RuntimeException {

    public BookIdMismatchException() {
        super("The book's id does not correspond to the data to be updated");
    }
}
