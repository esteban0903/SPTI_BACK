package co.edu.eci.blueprints.filters;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import co.edu.eci.blueprints.model.Blueprint;
import co.edu.eci.blueprints.model.Point;

class UndersamplingFilterTest {
    @Test
    void apply_emptyListReturnsSame() {
        Blueprint bp = new Blueprint("author", "name", List.of());
        UndersamplingFilter filter = new UndersamplingFilter();
        Blueprint filtered = filter.apply(bp);
        assertEquals(List.of(), filtered.getPoints());
    }

    @Test
    void apply_singlePointReturnsSame() {
        Blueprint bp = new Blueprint("author", "name", List.of(new Point(1,1)));
        UndersamplingFilter filter = new UndersamplingFilter();
        Blueprint filtered = filter.apply(bp);
        assertEquals(List.of(new Point(1,1)), filtered.getPoints());
    }
    @Test
    void apply_removesEvenIndexedPoints() {
        List<Point> points = Arrays.asList(
            new Point(1, 1), 
            new Point(2, 2), 
            new Point(3, 3), 
            new Point(4, 4), 
            new Point(5, 5), 
            new Point(6, 6)  
        );
        Blueprint bp = new Blueprint("author", "name", points);
        UndersamplingFilter filter = new UndersamplingFilter();
        Blueprint filtered = filter.apply(bp);
        List<Point> expected = Arrays.asList(
            new Point(1, 1),
            new Point(3, 3),
            new Point(5, 5)
        );
        assertEquals(expected, filtered.getPoints());
    }
}
