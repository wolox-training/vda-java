package wolox.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "User Id mismatch whit path params is")
public class UserIdMismatchException extends RuntimeException {

    public UserIdMismatchException() {

        super("The user's id does not correspond to the data to be updated");
    }
    public UserIdMismatchException(String message) {

        super(message);
    }
}
