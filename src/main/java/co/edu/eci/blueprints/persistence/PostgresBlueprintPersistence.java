package co.edu.eci.blueprints.persistence;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import co.edu.eci.blueprints.model.Blueprint;
import co.edu.eci.blueprints.model.Point;


@Primary
@Repository
/**
 * Implementación de persistencia de blueprints usando PostgreSQL y Spring Data JPA.
 * Proporciona operaciones CRUD y gestión de puntos en planos.
 */
public class PostgresBlueprintPersistence implements BlueprintPersistence {

    /**
     * Repositorio JPA para acceso a datos de blueprints.
     */
    private final BlueprintRepository blueprintRepository;

    /**
     * Constructor con inyección de dependencias.
     * @param blueprintRepository Repositorio JPA de blueprints
     */
    public PostgresBlueprintPersistence(BlueprintRepository blueprintRepository) {
        this.blueprintRepository = blueprintRepository;
    }

    /**
     * Guarda un nuevo blueprint en la base de datos.
     * @param bp Blueprint a guardar
     * @throws BlueprintPersistenceException si ya existe un blueprint con el mismo autor y nombre
     */
    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprintRepository.findByAuthorAndName(bp.getAuthor(), bp.getName()) != null) {
            throw new BlueprintPersistenceException("Blueprint already exists: " + bp.getAuthor() + "/" + bp.getName());
        }
        blueprintRepository.save(bp);
    }

    /**
     * Obtiene un blueprint por autor y nombre.
     * @param author Autor del blueprint
     * @param name Nombre del blueprint
     * @return Blueprint encontrado
     * @throws BlueprintNotFoundException si no existe el blueprint solicitado
     */
    @Override
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        Blueprint bp = blueprintRepository.findByAuthorAndName(author, name);
        if (bp == null) throw new BlueprintNotFoundException("Blueprint not found: %s/%s".formatted(author, name));
        return bp;
    }

    /**
     * Obtiene todos los blueprints de un autor.
     * @param author Autor de los blueprints
     * @return Set de blueprints
     * @throws BlueprintNotFoundException si el autor no tiene planos
     */
    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> set = blueprintRepository.findByAuthor(author);
        if (set.isEmpty()) throw new BlueprintNotFoundException("No blueprints for author: " + author);
        return set;
    }

    /**
     * Obtiene todos los blueprints almacenados.
     * @return Set de todos los blueprints
     */
    @Override
    public Set<Blueprint> getAllBlueprints() {
        return new HashSet<>(blueprintRepository.findAll());
    }

    /**
     * Agrega un punto a un blueprint existente.
     * @param author Autor del blueprint
     * @param name Nombre del blueprint
     * @param x Coordenada X del punto
     * @param y Coordenada Y del punto
     * @throws BlueprintNotFoundException si el blueprint no existe
     */
    @Override
    public void addPoint(String author, String name, int x, int y) throws BlueprintNotFoundException {
        Blueprint bp = getBlueprint(author, name);
        bp.addPoint(new Point(x, y));
        blueprintRepository.save(bp); // Guardar los cambios en la base de datos
    }
}
