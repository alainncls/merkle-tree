package fr.alainncls.merkeltree.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MerkelTreeNotFoundException extends RuntimeException {

    public MerkelTreeNotFoundException() {
        super("Merkel Tree not found");
    }
}
