package co.edu.eci.blueprints.controllers;

/**
 * DTO para respuestas estándar de la API REST.
 * @param <T> Tipo de dato devuelto en la respuesta.
 * @param code Código de estado HTTP o de negocio.
 * @param message Mensaje descriptivo de la respuesta.
 * @param data Datos devueltos por la operación (puede ser null).
 */
public record ApiResponseDTO<T>(int code, String message, T data) {

}
