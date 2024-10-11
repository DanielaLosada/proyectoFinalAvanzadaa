package co.edu.uniquindio.UniEventos.dto.CarritoDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

public record CrearCarritoDTO(
        @NotBlank(message = "El ID no puede estar vacío") String idUser,
        @NotNull(message = "La fecha no puede ser nula") LocalDateTime fecha,
        @NotNull(message = "La lista de artículos no puede ser nula") List<ItemCarritoDTO> items
) {
}
