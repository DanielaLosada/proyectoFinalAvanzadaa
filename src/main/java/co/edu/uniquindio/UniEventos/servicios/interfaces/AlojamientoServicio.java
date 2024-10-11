package co.edu.uniquindio.UniEventos.servicios.interfaces;

import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.CrearAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.EditarAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.InfoAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.EventoDTO.EditarEventoDTO;
import co.edu.uniquindio.UniEventos.dto.EventoDTO.InfoEventoDTO;

import java.util.List;

public interface AlojamientoServicio {
    String crearAlojamiento(CrearAlojamientoDTO alojamiento) throws Exception;

    String editarAlojamiento(EditarAlojamientoDTO editarAlojamientoDTO, String id) throws Exception;

    String eliminarAlojamiento(String id) throws Exception;

    InfoAlojamientoDTO obtenerInformacionAlojamiento(String id) throws Exception;

    List<InfoAlojamientoDTO> listarAlojamientos();

}
