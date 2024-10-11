package co.edu.uniquindio.UniEventos.dto.AlojamientoDTO;

import co.edu.uniquindio.UniEventos.modelo.TipoAlojamiento;

public record InfoAlojamientoDTO(
        String nombre,
        String ciudad,
        float calificacion,
        float precioNoche,
        float distanciaEvento,
        TipoAlojamiento tipo
) {
}
