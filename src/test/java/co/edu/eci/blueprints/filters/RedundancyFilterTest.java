package co.edu.eci.blueprints.filters;

import co.edu.eci.blueprints.model.Blueprint;
import co.edu.eci.blueprints.model.Point;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class RedundancyFilterTest {
    @Test
    void apply_emptyListReturnsSame() {
        Blueprint bp = new Blueprint("author", "name", List.of());
        RedundancyFilter filter = new RedundancyFilter();
        Blueprint filtered = filter.apply(bp);
        assertEquals(List.of(), filtered.getPoints());
    }

    @Test
    void apply_singlePointReturnsSame() {
        Blueprint bp = new Blueprint("author", "name", List.of(new Point(1,1)));
        RedundancyFilter filter = new RedundancyFilter();
        Blueprint filtered = filter.apply(bp);
        assertEquals(List.of(new Point(1,1)), filtered.getPoints());
    }
    @Test
    void apply_removesConsecutiveDuplicates() {
        List<Point> points = Arrays.asList(
            new Point(1, 1),
            new Point(2, 2),
            new Point(2, 2),
            new Point(3, 3),
            new Point(3, 3),
            new Point(4, 4)
        );
        Blueprint bp = new Blueprint("author", "name", points);
        RedundancyFilter filter = new RedundancyFilter();
        Blueprint filtered = filter.apply(bp);
        List<Point> expected = Arrays.asList(
            new Point(1, 1),
            new Point(2, 2),
            new Point(3, 3),
            new Point(4, 4)
        );
        assertEquals(expected, filtered.getPoints());
    }
}
