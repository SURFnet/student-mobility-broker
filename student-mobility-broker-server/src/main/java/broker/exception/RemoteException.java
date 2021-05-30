package broker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

public class RemoteException extends ResponseStatusException {

    public RemoteException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
