package co.edu.eci.blueprints.filters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import co.edu.eci.blueprints.model.Blueprint;
import co.edu.eci.blueprints.model.Point;

/**
 * Filtro de redundancia: elimina puntos consecutivos duplicados (x,y) en el blueprint.
 * Reduce la cantidad de datos repetidos y optimiza el plano.
 * Perfil Spring: "redundancy"
 */
@Component
@Profile("redundancy")
public class RedundancyFilter implements BlueprintsFilter {
    @Override
    public Blueprint apply(Blueprint bp) {
    var in = bp.getPoints();
    if (in == null || in.size() < 2) return bp;   // 👈 guard

    List<Point> out = new ArrayList<>();
    Point prev = null;
    for (Point p : in) {
        if (prev == null || !(prev.x() == p.x() && prev.y() == p.y())) {
            out.add(p);
            prev = p;
        }
    }
    return new Blueprint(bp.getAuthor(), bp.getName(), out);
    }
}
