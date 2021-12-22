package fr.alainncls.merkletree.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MerkleTreeNotFoundException extends RuntimeException {

    public MerkleTreeNotFoundException() {
        super("Merkle Tree not found");
    }
}
