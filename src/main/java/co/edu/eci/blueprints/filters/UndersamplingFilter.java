package co.edu.eci.blueprints.filters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import co.edu.eci.blueprints.model.Blueprint;
import co.edu.eci.blueprints.model.Point;

/**
 * Filtro de undersampling: conserva 1 de cada 2 puntos (índices pares) en el blueprint.
 * Reduce la densidad de puntos para simplificar el plano.
 * Perfil Spring: "undersampling"
 */
@Component
@Profile("undersampling")
public class UndersamplingFilter implements BlueprintsFilter {
    @Override
    public Blueprint apply(Blueprint bp) {
    var in = bp.getPoints();
    if (in == null || in.size() <= 2) return bp; 

    List<Point> out = new ArrayList<>();
    for (int i = 0; i < in.size(); i++) {
        if (i % 2 == 0) out.add(in.get(i));      
    }
    return new Blueprint(bp.getAuthor(), bp.getName(), out);
}
}
