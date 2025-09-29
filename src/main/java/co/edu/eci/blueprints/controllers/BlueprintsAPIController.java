package co.edu.eci.blueprints.controllers;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

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
@RequestMapping("/api/v1/blueprints")
@CrossOrigin(origins = "http://localhost:5173")

/**
 * Controlador REST para la gestión de planos (blueprints).
 * Proporciona endpoints para consultar, crear y modificar planos.
 */
public class BlueprintsAPIController {

    /**
     * Servicio principal para operaciones sobre blueprints.
     */
    private final BlueprintsServices services;

    /**
     * Constructor con inyección de dependencias.
     * @param services Servicio de blueprints
     */
    public BlueprintsAPIController(BlueprintsServices services) { this.services = services; }

    /**
     * Obtiene todos los planos almacenados.
     * @return ResponseEntity con el listado de blueprints y estado 200.
     */
    @Operation(summary = "Obtiene todos los planos", description = "Devuelve todos los blueprints almacenados")
    @ApiResponse(
        responseCode = "200",
        description = "Consulta exitosa",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = co.edu.eci.blueprints.model.Blueprint.class)
        )
    )
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Set<Blueprint>>> getAll() {
        Set<Blueprint> blueprints = services.getAllBlueprints();
        var response = new ApiResponseDTO<>(200, "execute ok", blueprints);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene los planos de un autor específico.
     * @param author Nombre del autor
     * @return ResponseEntity con el listado de planos o error 404 si no existe el autor.
     */
    @Operation(
    summary = "Obtiene los planos por autor",
    description = "Devuelve todos los blueprints de un autor específico"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Consulta exitosa",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = co.edu.eci.blueprints.model.Blueprint.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Autor no encontrado"
    )
    @GetMapping("/{author}")
    public ResponseEntity<?> byAuthor(@PathVariable String author) {
        try {
            Set<Blueprint> blueprints = services.getBlueprintsByAuthor(author);
            ApiResponseDTO<Set<Blueprint>> response = new ApiResponseDTO<>(200, "execute ok", blueprints);
            return ResponseEntity.ok(response);
        } catch (BlueprintNotFoundException e) {
            ApiResponseDTO<Set<Blueprint>> response = new ApiResponseDTO<>(404, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    /**
     * Obtiene un plano específico por autor y nombre.
     * @param author Nombre del autor
     * @param bpname Nombre del plano
     * @return ResponseEntity con el plano o error 404 si no existe.
     */
    @Operation(
        summary = "Obtiene un plano por autor y nombre",
        description = "Devuelve un blueprint específico dado el autor y el nombre"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Consulta exitosa",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = co.edu.eci.blueprints.model.Blueprint.class)
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
            schema = @Schema(implementation = co.edu.eci.blueprints.model.Blueprint.class)
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
     * Agrega un punto a un plano existente.
     * @param author Nombre del autor
     * @param bpname Nombre del plano
     * @param p Punto a agregar
     * @return ResponseEntity con estado 202 si se agrega, o 404 si no existe el plano.
     */
    @Operation(
        summary = "Agrega un punto a un plano existente",
        description = "Añade un nuevo punto a un blueprint específico"
    )
    @ApiResponse(
        responseCode = "202",
        description = "Punto agregado",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = co.edu.eci.blueprints.model.Blueprint.class)
        )
    )
    @ApiResponse(
        responseCode = "404",
        description = "Plano no encontrado"
    )
    @PutMapping("/{author}/{bpname}/points")
    public ResponseEntity<?> addPoint(@PathVariable String author, @PathVariable String bpname,
                                      @RequestBody Point p) {
        try {
            services.addPoint(author, bpname, p.x(), p.y());
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO(202, "point added", null);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (BlueprintNotFoundException e) {
            ApiResponseDTO<Blueprint> response = new ApiResponseDTO(404, e.getMessage() , null);
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
}
