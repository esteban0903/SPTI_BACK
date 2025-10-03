package co.edu.eci.blueprints.controllers;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.eci.blueprints.model.Blueprint;
import co.edu.eci.blueprints.model.Point;
import co.edu.eci.blueprints.persistence.BlueprintNotFoundException;
import co.edu.eci.blueprints.persistence.BlueprintPersistenceException;
import co.edu.eci.blueprints.services.BlueprintsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/api/v1/public/blueprints")
@CrossOrigin(origins = "http://localhost:5173")
/**
 * Controlador REST público para la gestión de planos (blueprints).
 * Proporciona endpoints sin autenticación para desarrollo y testing.
 */
public class PublicBlueprintsAPIController {

    /**
     * Servicio principal para operaciones sobre blueprints.
     */
    private final BlueprintsServices services;

    /**
     * Constructor con inyección de dependencias.
     * @param services Servicio de blueprints
     */
    public PublicBlueprintsAPIController(BlueprintsServices services) { 
        this.services = services; 
    }

    /**
     * Obtiene todos los planos almacenados.
     * @return ResponseEntity con el listado de blueprints y estado 200.
     */
    @Operation(
        summary = "Obtiene todos los planos",
        description = "Devuelve la lista completa de blueprints almacenados en el sistema"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de planos",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Blueprint.class)
        )
    )
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Set<Blueprint>>> blueprints() {
        Set<Blueprint> data = services.getAllBlueprints();
        ApiResponseDTO<Set<Blueprint>> response = new ApiResponseDTO<>(200, "execute ok", data);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todos los planos de un autor específico.
     * @param author Nombre del autor
     * @return ResponseEntity con los planos del autor y estado 200.
     */
    @Operation(
        summary = "Obtiene los planos de un autor",
        description = "Devuelve todos los blueprints creados por un autor específico"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de planos del autor",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Blueprint.class)
        )
    )
    @GetMapping("/{author}")
    public ResponseEntity<ApiResponseDTO<Set<Blueprint>>> blueprintsByAuthor(@PathVariable String author) {
        try {
            Set<Blueprint> data = services.getBlueprintsByAuthor(author);
            ApiResponseDTO<Set<Blueprint>> response = new ApiResponseDTO<>(200, "execute ok", data);
            return ResponseEntity.ok(response);
        } catch (BlueprintNotFoundException e) {
            ApiResponseDTO<Set<Blueprint>> response = new ApiResponseDTO<>(404, "Author not found", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Obtiene un plano específico por autor y nombre.
     * @param author Nombre del autor
     * @param bpname Nombre del plano
     * @return ResponseEntity con el plano solicitado y estado 200, o 404 si no se encuentra.
     */
    @Operation(
        summary = "Obtiene un plano específico",
        description = "Devuelve un blueprint específico identificado por autor y nombre"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Plano encontrado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Blueprint.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plano no encontrado"
    )
    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<ApiResponseDTO<Blueprint>> byAuthorAndName(@PathVariable String author, @PathVariable String bpname) {
        try {
            Blueprint blueprint = services.getBlueprint(author, bpname);
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO<>(200, "execute ok", blueprint);
            return ResponseEntity.ok(response);
        } catch (BlueprintNotFoundException e) {
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO<>(404, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Crea un nuevo plano en el sistema.
     * @param req Datos del nuevo plano
     * @return ResponseEntity con el plano creado o error 400 si falla la persistencia.
     */
    @Operation(
        summary = "Crea un nuevo plano",
        description = "Agrega un nuevo blueprint al sistema"
    )
    @ApiResponse(
        responseCode = "201",
        description = "Plano creado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Blueprint.class)
        )
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error de persistencia"
    )
    @PostMapping
    public ResponseEntity<ApiResponseDTO<Blueprint>> add(@Valid @RequestBody NewBlueprintRequest req) {
        try {
            Blueprint bp = new Blueprint(req.author(), req.name(), req.points());
            services.addNewBlueprint(bp);
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO<>(201, "created", bp);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (BlueprintPersistenceException e) {
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO<>(400, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Actualiza un plano existente.
     * @param author Autor original del plano
     * @param bpname Nombre original del plano
     * @param req Datos actualizados del plano
     * @return ResponseEntity con estado 200 si se actualiza, o error 404/400.
     */
    @Operation(
        summary = "Actualiza un plano existente",
        description = "Modifica un blueprint existente con nuevos datos"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Plano actualizado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Blueprint.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plano no encontrado"
    )
    @ApiResponse(
        responseCode = "400",
        description = "Error de persistencia"
    )
    @PutMapping("/{author}/{bpname}")
    public ResponseEntity<ApiResponseDTO<Blueprint>> updateBlueprint(
            @PathVariable String author, 
            @PathVariable String bpname,
            @Valid @RequestBody UpdateBlueprintRequest req) {
        try {
            Blueprint updatedBp = new Blueprint(req.author(), req.name(), req.points());
            services.updateBlueprint(author, bpname, updatedBp);
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO<>(200, "updated", updatedBp);
            return ResponseEntity.ok(response);
        } catch (BlueprintNotFoundException e) {
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO<>(404, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (BlueprintPersistenceException e) {
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO<>(400, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Elimina un plano existente.
     * @param author Autor del plano
     * @param bpname Nombre del plano
     * @return ResponseEntity con estado 200 si se elimina, o error 404.
     */
    @Operation(
        summary = "Elimina un plano existente",
        description = "Elimina un blueprint del sistema"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Plano eliminado"
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plano no encontrado"
    )
    @DeleteMapping("/{author}/{bpname}")
    public ResponseEntity<ApiResponseDTO<Void>> deleteBlueprint(
            @PathVariable String author, 
            @PathVariable String bpname) {
        try {
            services.deleteBlueprint(author, bpname);
            ApiResponseDTO<Void> response = new ApiResponseDTO<>(200, "deleted", null);
            return ResponseEntity.ok(response);
        } catch (BlueprintNotFoundException e) {
            ApiResponseDTO<Void> response = new ApiResponseDTO<>(404, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * DTO para la creación de nuevos planos.
     * @param author Autor del plano
     * @param name Nombre del plano
     * @param points Lista de puntos del plano
     */
    public record NewBlueprintRequest(
            @NotBlank String author,
            @NotBlank String name,
            @Valid java.util.List<Point> points
    ) { }

    /**
     * DTO para la actualización de planos existentes.
     * @param author Autor del plano
     * @param name Nombre del plano
     * @param points Lista de puntos del plano
     */
    public record UpdateBlueprintRequest(
            @NotBlank String author,
            @NotBlank String name,
            @Valid java.util.List<Point> points
    ) { }

    /**
     * DTO genérico para respuestas de la API.
     * @param <T> Tipo de datos contenidos en la respuesta
     */
    public static class ApiResponseDTO<T> {
        private final int statusCode;
        private final String message;
        private final T data;

        public ApiResponseDTO(int statusCode, String message, T data) {
            this.statusCode = statusCode;
            this.message = message;
            this.data = data;
        }

        public int getStatusCode() { return statusCode; }
        public String getMessage() { return message; }
        public T getData() { return data; }
    }
}