
package co.edu.eci.blueprints.persistence;

import java.util.Set;

import co.edu.eci.blueprints.model.Blueprint;

/**
 * Interfaz para la persistencia de blueprints.
 * Define las operaciones CRUD y gestión de puntos en planos.
 */
public interface BlueprintPersistence {

    /**
     * Guarda un nuevo blueprint.
     * @param bp Blueprint a guardar
     * @throws BlueprintPersistenceException si ya existe el blueprint
     */
    void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException;

    /**
     * Obtiene un blueprint por autor y nombre.
     * @param author Autor del blueprint
     * @param name Nombre del blueprint
     * @return Blueprint encontrado
     * @throws BlueprintNotFoundException si no existe el blueprint
     */
    Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException;

    /**
     * Obtiene todos los blueprints de un autor.
     * @param author Autor
     * @return Set de blueprints
     * @throws BlueprintNotFoundException si el autor no tiene planos
     */
    Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException;

    /**
     * Obtiene todos los blueprints almacenados.
     * @return Set de todos los blueprints
     */
    Set<Blueprint> getAllBlueprints();

    /**
     * Agrega un punto a un blueprint existente.
     * @param author Autor del blueprint
     * @param name Nombre del blueprint
     * @param x Coordenada X
     * @param y Coordenada Y
     * @throws BlueprintNotFoundException si el blueprint no existe
     */
    void addPoint(String author, String name, int x, int y) throws BlueprintNotFoundException;

    /**
     * Actualiza un blueprint existente.
     * @param originalAuthor Autor original del blueprint
     * @param originalName Nombre original del blueprint
     * @param updatedBlueprint Blueprint con los nuevos datos
     * @throws BlueprintNotFoundException si el blueprint original no existe
     * @throws BlueprintPersistenceException si hay problemas de persistencia
     */
    void updateBlueprint(String originalAuthor, String originalName, Blueprint updatedBlueprint) 
        throws BlueprintNotFoundException, BlueprintPersistenceException;

    /**
     * Elimina un blueprint.
     * @param author Autor del blueprint
     * @param name Nombre del blueprint
     * @throws BlueprintNotFoundException si el blueprint no existe
     */
    void deleteBlueprint(String author, String name) throws BlueprintNotFoundException;
}
