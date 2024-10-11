package co.edu.uniquindio.UniEventos.dto.CarritoDTO;

import co.edu.uniquindio.UniEventos.modelo.DetalleCarrito;

import java.time.LocalDateTime;
import java.util.List;

public record InfoCarritoDTO(
        LocalDateTime fecha,
        List<DetalleCarrito> items

) {
}
