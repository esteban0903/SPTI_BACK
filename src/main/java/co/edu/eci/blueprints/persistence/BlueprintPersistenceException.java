
package co.edu.eci.blueprints.persistence;

/**
 * Excepción lanzada cuando ocurre un error de persistencia al guardar un blueprint.
 */
public class BlueprintPersistenceException extends Exception {
    /**
     * Constructor con mensaje personalizado.
     * @param msg Mensaje descriptivo del error
     */
    public BlueprintPersistenceException(String msg) { super(msg); }
}
