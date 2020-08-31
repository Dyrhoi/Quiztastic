package quiztastic.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void shouldNormalizeNames() {
        assertEquals(new Category("Hello"), new Category("HELLO"));
    }

}