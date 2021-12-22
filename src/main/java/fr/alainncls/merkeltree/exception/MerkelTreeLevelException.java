package fr.alainncls.merkeltree.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MerkelTreeLevelException extends RuntimeException {

    public MerkelTreeLevelException() {
        super("This level doesn't exist in the given Merkel tree");
    }
}
