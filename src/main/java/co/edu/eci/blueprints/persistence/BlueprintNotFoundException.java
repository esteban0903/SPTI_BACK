
package co.edu.eci.blueprints.persistence;

/**
 * Excepción lanzada cuando no se encuentra un blueprint solicitado.
 */
public class BlueprintNotFoundException extends Exception {
    /**
     * Constructor con mensaje personalizado.
     * @param msg Mensaje descriptivo del error
     */
    public BlueprintNotFoundException(String msg) { super(msg); }
}
