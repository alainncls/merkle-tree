package fr.alainncls.merkletree;

import fr.alainncls.merkletree.controller.MerkleTreeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MerkleTreeApplicationTests {

    @Autowired
    MerkleTreeController merkleTreeController;

    @Test
    void contextLoads() {
        assertNotNull(merkleTreeController);
    }

}
