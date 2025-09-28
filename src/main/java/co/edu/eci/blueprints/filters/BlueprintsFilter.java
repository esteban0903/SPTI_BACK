
package co.edu.eci.blueprints.filters;

import co.edu.eci.blueprints.model.Blueprint;

/**
 * Interfaz para filtros de planos (blueprints).
 * Permite aplicar transformaciones sobre un blueprint antes de ser retornado por el servicio.
 */
public interface BlueprintsFilter {
    /**
     * Aplica el filtro sobre el blueprint recibido.
     * @param bp Blueprint original
     * @return Blueprint filtrado o transformado
     */
    Blueprint apply(Blueprint bp);
}
