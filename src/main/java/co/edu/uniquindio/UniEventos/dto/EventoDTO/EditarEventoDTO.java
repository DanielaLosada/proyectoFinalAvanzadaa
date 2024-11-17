package co.edu.uniquindio.UniEventos.dto.EventoDTO;

import co.edu.uniquindio.UniEventos.modelo.Alojamiento;
import co.edu.uniquindio.UniEventos.modelo.EstadoEvento;
import co.edu.uniquindio.UniEventos.modelo.Localidad;
import co.edu.uniquindio.UniEventos.modelo.TipoEvento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

public record EditarEventoDTO(
        @NotBlank(message = "El ID del evento es obligatorio") String id,
        @NotBlank(message = "El nombre del evento es obligatorio") @Size(max = 150, message = "El nombre del evento no puede exceder los 150 caracteres") String nombre,
        @NotBlank(message = "La descripción es obligatoria") @Size(max = 800, message = "La descripción no puede exceder los 800 caracteres") String descripcion,
        @NotBlank(message = "La dirección es obligatoria") @Size(max = 350, message = "La dirección no puede exceder los 350 caracteres") String direccion,
        @NotBlank(message = "La ciudad es obligatoria") @Size(max = 60, message = "La ciudad no puede exceder los 60 caracteres") String ciudad,
        @NotNull(message = "La fecha es obligatoria") LocalDateTime fecha,
        @NotBlank(message = "El tipo de evento es obligatorio") TipoEvento tipo,
        @NotBlank(message = "El estado del evento es obligatorio") EstadoEvento estado,
        @URL(message = "La URL de la imagen del poster debe ser válida") String imagenPoster,
        @URL(message = "La URL de la imagen de localidades debe ser válida") String imagenLocalidades,
        @NotNull(message = "La lista de localidades es obligatoria") List<LocalidadDTO> localidades
) {
}
