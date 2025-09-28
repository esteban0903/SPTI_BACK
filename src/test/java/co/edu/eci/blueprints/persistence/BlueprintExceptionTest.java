package co.edu.eci.blueprints.persistence;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BlueprintNotFoundExceptionTest {
    @Test
    void exceptionMessageIsSet() {
        BlueprintNotFoundException ex = new BlueprintNotFoundException("not found");
        assertEquals("not found", ex.getMessage());
    }
}

class BlueprintPersistenceExceptionTest {
    @Test
    void exceptionMessageIsSet() {
        BlueprintPersistenceException ex = new BlueprintPersistenceException("exists");
        assertEquals("exists", ex.getMessage());
    }
}
