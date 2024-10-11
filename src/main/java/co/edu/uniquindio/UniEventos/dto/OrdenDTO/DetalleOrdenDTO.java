package co.edu.uniquindio.UniEventos.dto.OrdenDTO;

import java.time.LocalDateTime;

public record DetalleOrdenDTO(
        String ImagenEvento, String nombresEvento, LocalDateTime fecha, String localidadEvento, int cantidadEvento, float total
) {
}
