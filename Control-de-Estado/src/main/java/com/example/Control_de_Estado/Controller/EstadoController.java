package com.example.Control_de_Estado.Controller;

import com.example.Control_de_Estado.Model.Estado;
import com.example.Control_de_Estado.Service.EstadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/estados")
public class EstadoController {

    @Autowired
    private EstadoService estadoService;

    @Operation(summary = "Obtener todos los estados", description = "Devuelve una lista con todos los estados registrados")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estados encontrados", content = @Content(
            mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = Estado.class))
        )),
        @ApiResponse(responseCode = "204", description = "No hay estados disponibles", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Estado>> obtenerEstados() {
        List<Estado> estados = estadoService.ObtenerEstados();
        if (estados.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estados);
    }

    @Operation(summary = "Obtener estado por ID", description = "Devuelve un estado específico según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado encontrado", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Estado.class)
        )),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Estado> obtenerEstadoPorId(@PathVariable Long id) {
        Optional<Estado> estado = estadoService.obtenerEstadoPorId(id);
        return estado.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo estado",
        description = "Crea un nuevo estado en el sistema",
        requestBody = @RequestBody(
            description = "Datos del estado a crear",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Estado.class),
                examples = @ExampleObject(
                    name = "Ejemplo Estado",
                    value = "{ \"nombre\": \"Disponible\" }"
                )
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Estado creado correctamente", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Estado.class)
        )),
        @ApiResponse(responseCode = "400", description = "Error en los datos enviados", content = @Content(
            mediaType = "text/plain",
            schema = @Schema(type = "string", example = "Error al crear el estado: nombre vacío")
        ))
    })
    @PostMapping
    public ResponseEntity<?> crearEstado(@org.springframework.web.bind.annotation.RequestBody Estado estado) {
        try {
            Estado nuevo = estadoService.crearEstado(estado);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al crear el estado: " + e.getMessage());
        }
    }

    @Operation(
        summary = "Actualizar un estado",
        description = "Actualiza los datos de un estado existente",
        requestBody = @RequestBody(
            description = "Datos del estado actualizado",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Estado.class),
                examples = @ExampleObject(
                    name = "Ejemplo Actualización",
                    value = "{ \"nombre\": \"Entregado\" }"
                )
            )
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estado actualizado correctamente", content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Estado.class)
        )),
        @ApiResponse(responseCode = "400", description = "Error en los datos enviados", content = @Content(
            mediaType = "text/plain",
            schema = @Schema(type = "string", example = "Error al actualizar el estado: ID inválido")
        ))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEstadoPorId(
            @PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody Estado estadoActualizado) {
        try {
            Estado actualizado = estadoService.actualizarEstado(id, estadoActualizado);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al actualizar el estado: " + e.getMessage());
        }
    }

    @Operation(summary = "Eliminar estado", description = "Elimina un estado específico según su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Estado eliminado correctamente", content = @Content),
        @ApiResponse(responseCode = "404", description = "Estado no encontrado", content = @Content(
            mediaType = "text/plain",
            schema = @Schema(type = "string", example = "Error al eliminar el estado: ID no existe")
        ))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstadoPorId(@PathVariable Long id) {
        try {
            estadoService.eliminarEstado(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error al eliminar el estado: " + e.getMessage());
        }
    }
}
