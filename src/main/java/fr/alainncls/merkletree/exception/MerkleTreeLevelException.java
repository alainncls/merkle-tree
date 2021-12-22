package fr.alainncls.merkletree.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MerkleTreeLevelException extends RuntimeException {

    public MerkleTreeLevelException() {
        super("This level doesn't exist in the given Merkle tree");
    }
}
