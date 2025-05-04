package hei.school.prog3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlayerInformationMismatchException extends RuntimeException {
    public PlayerInformationMismatchException(String message) {
        super(message);
    }
}