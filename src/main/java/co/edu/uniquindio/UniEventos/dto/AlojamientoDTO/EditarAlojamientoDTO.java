package co.edu.uniquindio.UniEventos.dto.AlojamientoDTO;

import co.edu.uniquindio.UniEventos.modelo.EstadoAlojamiento;
import co.edu.uniquindio.UniEventos.modelo.TipoAlojamiento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EditarAlojamientoDTO(
        @NotBlank(message = "El nombre del alojamiento es obligatorio") @Size(max = 150, message = "El nombre del alojamiento no puede exceder los 150 caracteres") String nombre,
        @NotNull(message = "El precio por noche es obligatorio") float precioNoche,
        @NotNull(message = "La calificacion es obligatoria") float calificacion,
        @NotBlank(message = "El tipo de alojamiento es obligatorio") TipoAlojamiento tipo,
        @NotBlank(message = "El estado de alojamiento es obligatorio") EstadoAlojamiento estado
) {
}
