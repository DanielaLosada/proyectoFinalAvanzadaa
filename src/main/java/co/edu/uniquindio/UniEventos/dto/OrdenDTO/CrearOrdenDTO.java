package co.edu.uniquindio.UniEventos.dto.OrdenDTO;

import co.edu.uniquindio.UniEventos.modelo.DetalleOrden;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record CrearOrdenDTO(
        @NotNull(message = "El ID del usuario no puede estar vacío")
        String idUsuario,
        @NotNull LocalDateTime fecha,

        @NotNull String codigoPasarela,

        @NotNull PagoDTO pago,

        @NotNull float total,
        @NotEmpty(message = "La lista de items de orden no puede estar vacía")
        List<@NotNull(message = "Los items de orden no pueden ser nulos") DetalleOrden> itemsOrden,
        String codigoCupon
) {
}
