package co.edu.eci.blueprints.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import co.edu.eci.blueprints.filters.BlueprintsFilter;
import co.edu.eci.blueprints.model.Blueprint;
import co.edu.eci.blueprints.persistence.BlueprintNotFoundException;
import co.edu.eci.blueprints.persistence.BlueprintPersistence;
import co.edu.eci.blueprints.persistence.BlueprintPersistenceException;


@Service
/**
 * Servicio principal para la gestión de planos (blueprints).
 * Orquesta la persistencia y la aplicación de filtros sobre los planos.
 */
public class BlueprintsServices {

    /**
     * Componente de persistencia para operaciones CRUD de blueprints.
     */
    private final BlueprintPersistence persistence;
    /**
     * Filtro activo para transformar los blueprints antes de retornarlos.
     */
    private final BlueprintsFilter filter;

    /**
     * Constructor con inyección de dependencias.
     * @param persistence Componente de persistencia
     * @param filter Filtro de blueprints
     */
    public BlueprintsServices(BlueprintPersistence persistence, BlueprintsFilter filter) {
        this.persistence = persistence;
        this.filter = filter;
    }

    /**
     * Agrega un nuevo blueprint al sistema.
     * @param bp Blueprint a agregar
     * @throws BlueprintPersistenceException si ya existe el blueprint
     */
    public void addNewBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        persistence.saveBlueprint(bp);
    }

    /**
     * Obtiene todos los blueprints aplicando el filtro configurado.
     * @return Set de blueprints filtrados
     */
    public Set<Blueprint> getAllBlueprints() {
        return persistence.getAllBlueprints()
            .stream()
            .map(filter::apply)
            .collect(Collectors.toSet());
    }

    /**
     * Obtiene los blueprints de un autor aplicando el filtro configurado.
     * @param author Autor de los blueprints
     * @return Set de blueprints filtrados
     * @throws BlueprintNotFoundException si el autor no tiene planos
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        return persistence.getBlueprintsByAuthor(author)
            .stream()
            .map(filter::apply)
            .collect(Collectors.toSet());
    }

    /**
     * Obtiene un blueprint específico aplicando el filtro configurado.
     * @param author Autor del blueprint
     * @param name Nombre del blueprint
     * @return Blueprint filtrado
     * @throws BlueprintNotFoundException si no existe el blueprint
     */
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        return filter.apply(persistence.getBlueprint(author, name));
    }

    /**
     * Agrega un punto a un blueprint existente.
     * @param author Autor del blueprint
     * @param name Nombre del blueprint
     * @param x Coordenada X del punto
     * @param y Coordenada Y del punto
     * @throws BlueprintNotFoundException si el blueprint no existe
     */
    public void addPoint(String author, String name, int x, int y) throws BlueprintNotFoundException {
        persistence.addPoint(author, name, x, y);
    }
}
