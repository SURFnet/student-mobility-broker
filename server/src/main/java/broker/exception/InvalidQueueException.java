package broker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InvalidQueueException extends RuntimeException {

    public InvalidQueueException(String message) {
        super(message);
    }
}
