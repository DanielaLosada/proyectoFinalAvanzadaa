package co.edu.uniquindio.UniEventos.dto.EventoDTO;

import co.edu.uniquindio.UniEventos.modelo.Localidad;
import co.edu.uniquindio.UniEventos.modelo.TipoEvento;

import java.time.LocalDateTime;
import java.util.List;

public record FiltroEventoDTO(

        String nombre,
        TipoEvento tipoEvento,
        String ciudad
) {
}
