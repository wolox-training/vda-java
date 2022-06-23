package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User Not Found")
public class UserIdMismatchException extends RuntimeException {

    public UserIdMismatchException() {

        super();
    }
    public UserIdMismatchException(String message, Throwable cause) {

        super(message, cause);
    }
    public UserIdMismatchException(String message) {

        super(message);
    }
    public UserIdMismatchException(Throwable cause) {

        super(cause);
    }
}
