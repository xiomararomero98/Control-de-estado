package com.example.Control_de_Estado.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "estado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Modelo que representa un estado en el sistema")
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    @Schema(description = "Identificador único del estado", example = "1")
    private Long id;

    @NotBlank(message = "El nombre del estado no puede estar vacío")
    @Column(nullable = false)
    @Schema(description = "Nombre del estado (ej: Disponible, Entregado, Agotado)", example = "Disponible")
    private String nombre;
}
