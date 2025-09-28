package co.edu.eci.blueprints.persistence;

import co.edu.eci.blueprints.model.Blueprint;
import co.edu.eci.blueprints.model.Point;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class PostgresBlueprintPersistenceTest {
    @Test
    void saveAndGetBlueprintShouldWork() throws Exception {
        BlueprintRepository repo = Mockito.mock(BlueprintRepository.class);
        PostgresBlueprintPersistence persistence = new PostgresBlueprintPersistence(repo);
    Blueprint bp = new Blueprint("author", "bp1", java.util.List.of(new Point(1,1)));
    Mockito.when(repo.findByAuthorAndName("author", "bp1")).thenReturn(null).thenReturn(bp);
    Mockito.when(repo.save(bp)).thenReturn(bp);
        persistence.saveBlueprint(bp);
        Blueprint result = persistence.getBlueprint("author", "bp1");
        assertEquals(bp, result);
    }

    @Test
    void getBlueprintsByAuthorShouldReturnSet() throws Exception {
        BlueprintRepository repo = Mockito.mock(BlueprintRepository.class);
        PostgresBlueprintPersistence persistence = new PostgresBlueprintPersistence(repo);
        Set<Blueprint> set = new HashSet<>();
    set.add(new Blueprint("author", "bp1", java.util.List.of(new Point(1,1))));
        Mockito.when(repo.findByAuthor("author")).thenReturn(set);
        Set<Blueprint> result = persistence.getBlueprintsByAuthor("author");
        assertEquals(set, result);
    }
}
