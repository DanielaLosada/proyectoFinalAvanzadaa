package co.edu.uniquindio.UniEventos.dto.OrdenDTO;

import java.time.LocalDateTime;

public record ItemOrdenDTO(
        String evento,
        String localidad,

        int cantidad,

        float precio
) {
}
