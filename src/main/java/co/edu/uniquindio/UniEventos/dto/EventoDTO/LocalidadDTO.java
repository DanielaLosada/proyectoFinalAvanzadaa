package co.edu.uniquindio.UniEventos.dto.EventoDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LocalidadDTO(
        @NotBlank(message = "El nombre no puede estar vacio")
        String nombre,
        @NotNull(message = "Debes ingresar la capacidad de la localidad")
        int aforo,
        @NotNull(message = "Debes agregar un precio")
        float precio
) {
}


