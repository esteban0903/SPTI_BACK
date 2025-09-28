package co.edu.eci.blueprints.services;

import co.edu.eci.blueprints.filters.IdentityFilter;
import co.edu.eci.blueprints.filters.BlueprintsFilter;
import co.edu.eci.blueprints.persistence.BlueprintPersistence;
import co.edu.eci.blueprints.model.Blueprint;
import co.edu.eci.blueprints.model.Point;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import co.edu.eci.blueprints.filters.RedundancyFilter;
import co.edu.eci.blueprints.filters.UndersamplingFilter;
import static org.junit.jupiter.api.Assertions.*;

class BlueprintsServicesTest {
    @Test
    void getBlueprintShouldApplyFilter() throws Exception {
        BlueprintPersistence persistence = Mockito.mock(BlueprintPersistence.class);
        BlueprintsFilter filter = new IdentityFilter();
        BlueprintsServices services = new BlueprintsServices(persistence, filter);
        List<Point> points = Arrays.asList(new Point(1,1), new Point(2,2));
        Blueprint bp = new Blueprint("author", "bp1", points);
        Mockito.when(persistence.getBlueprint("author", "bp1")).thenReturn(bp);
        Blueprint result = services.getBlueprint("author", "bp1");
        assertEquals(points, result.getPoints(), "El filtro debe aplicarse correctamente (IdentityFilter)");
    }

        @Test
        void addNewBlueprintShouldCallPersistence() throws Exception {
            BlueprintPersistence persistence = Mockito.mock(BlueprintPersistence.class);
            BlueprintsFilter filter = new IdentityFilter();
            BlueprintsServices services = new BlueprintsServices(persistence, filter);
            Blueprint bp = new Blueprint("author", "bp2", Arrays.asList(new Point(3,3)));
            services.addNewBlueprint(bp);
            Mockito.verify(persistence).saveBlueprint(bp);
        }

        @Test
        void getAllBlueprintsShouldApplyFilter() {
            BlueprintPersistence persistence = Mockito.mock(BlueprintPersistence.class);
            BlueprintsFilter filter = new RedundancyFilter();
            BlueprintsServices services = new BlueprintsServices(persistence, filter);
            Blueprint bp1 = new Blueprint("author", "bp1", Arrays.asList(new Point(1,1), new Point(1,1), new Point(2,2)));
            Blueprint bp2 = new Blueprint("author", "bp2", Arrays.asList(new Point(3,3), new Point(3,3)));
            Mockito.when(persistence.getAllBlueprints()).thenReturn(Set.of(bp1, bp2));
            Set<Blueprint> result = services.getAllBlueprints();
            assertEquals(2, result.size());
            for (Blueprint bp : result) {
                if (bp.getName().equals("bp1")) {
                    assertEquals(Arrays.asList(new Point(1,1), new Point(2,2)), bp.getPoints());
                } else if (bp.getName().equals("bp2")) {
                    assertEquals(Arrays.asList(new Point(3,3)), bp.getPoints());
                }
            }
        }

        @Test
        void getBlueprintsByAuthorShouldApplyFilter() throws Exception {
            BlueprintPersistence persistence = Mockito.mock(BlueprintPersistence.class);
            BlueprintsFilter filter = new UndersamplingFilter();
            BlueprintsServices services = new BlueprintsServices(persistence, filter);
            Blueprint bp1 = new Blueprint("author", "bp1", Arrays.asList(new Point(1,1), new Point(2,2), new Point(3,3), new Point(4,4)));
            Mockito.when(persistence.getBlueprintsByAuthor("author")).thenReturn(Set.of(bp1));
            Set<Blueprint> result = services.getBlueprintsByAuthor("author");
            assertEquals(1, result.size());
            Blueprint filtered = result.iterator().next();
            assertEquals(Arrays.asList(new Point(1,1), new Point(3,3)), filtered.getPoints());
        }

        @Test
        void addPointShouldCallPersistence() throws Exception {
            BlueprintPersistence persistence = Mockito.mock(BlueprintPersistence.class);
            BlueprintsFilter filter = new IdentityFilter();
            BlueprintsServices services = new BlueprintsServices(persistence, filter);
            services.addPoint("author", "bp1", 5, 5);
            Mockito.verify(persistence).addPoint("author", "bp1", 5, 5);
        }
}
