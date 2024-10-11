package co.edu.uniquindio.UniEventos.dto.EventoDTO;

import co.edu.uniquindio.UniEventos.modelo.Alojamiento;
import co.edu.uniquindio.UniEventos.modelo.Localidad;
import co.edu.uniquindio.UniEventos.modelo.TipoEvento;

import java.time.LocalDateTime;
import java.util.List;

public record InfoEventoDTO(
        String imagenPortada,
        String nombre,
        String descripcion,
        String direccion,
        TipoEvento tipoEvento,
        LocalDateTime fecha,
        String ciudad,
        List<Localidad> listaLocalidades,
        List<Alojamiento> alojamientosCercanos
) {
}
