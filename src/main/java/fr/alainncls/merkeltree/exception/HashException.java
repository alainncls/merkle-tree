package fr.alainncls.merkeltree.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class HashException extends RuntimeException {

    public HashException(String message) {
        super(message);
    }
}
