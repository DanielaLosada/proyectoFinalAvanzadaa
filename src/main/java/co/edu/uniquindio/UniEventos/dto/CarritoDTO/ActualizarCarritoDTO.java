package co.edu.uniquindio.UniEventos.dto.CarritoDTO;

import co.edu.uniquindio.UniEventos.modelo.DetalleCarrito;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.List;

public record ActualizarCarritoDTO(
        @Positive int cantidad, // Cambiado a quantity
        @NotBlank @Length(max = 100) String nombreLocalidad
) {
}
