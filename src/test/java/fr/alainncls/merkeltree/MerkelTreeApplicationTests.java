package fr.alainncls.merkeltree;

import fr.alainncls.merkeltree.controller.MerkelTreeController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class MerkelTreeApplicationTests {

    @Autowired
    MerkelTreeController merkelTreeController;

    @Test
    void contextLoads() {
        assertNotNull(merkelTreeController);
    }

}
