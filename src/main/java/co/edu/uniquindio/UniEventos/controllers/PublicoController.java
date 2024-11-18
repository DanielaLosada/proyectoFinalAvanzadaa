package co.edu.uniquindio.UniEventos.controllers;

import co.edu.uniquindio.UniEventos.dto.EmailDTO.MensajeDTO;
import co.edu.uniquindio.UniEventos.dto.EventoDTO.FiltroEventoDTO;
import co.edu.uniquindio.UniEventos.dto.EventoDTO.InfoEventoDTO;
import co.edu.uniquindio.UniEventos.dto.EventoDTO.ItemEventoDTO;
import co.edu.uniquindio.UniEventos.modelo.Cuenta;
import co.edu.uniquindio.UniEventos.modelo.Evento;
import co.edu.uniquindio.UniEventos.modelo.TipoEvento;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CuentaServicio;
import co.edu.uniquindio.UniEventos.servicios.interfaces.EventoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class PublicoController {

    private final EventoServicio eventoServicio;

    @GetMapping("/evento/listar-eventos")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> listarEventos() {
        List<ItemEventoDTO> eventos = eventoServicio.listarEventos();
        return ResponseEntity.ok(new MensajeDTO<>(false, eventos));
    }
    @GetMapping("/evento/obtener-tipos")
    public ResponseEntity<MensajeDTO<List<String>>> listarTipos() {
        List<String> tipos = eventoServicio.listarTipos();
        return ResponseEntity.ok(new MensajeDTO<>(false, tipos));
    }

    @GetMapping("/evento/obtener-evento/{nombre}")
    public ResponseEntity<MensajeDTO<InfoEventoDTO>> obtenerEvento(@PathVariable String nombre) throws Exception {
        InfoEventoDTO evento = eventoServicio.obtenerInformacionEvento(nombre);
        return ResponseEntity.ok(new MensajeDTO<>(false, evento));
    }

    @GetMapping("/evento/filtrar-eventos")
    public ResponseEntity<MensajeDTO<List<ItemEventoDTO>>> filtrarEventos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) TipoEvento tipoEvento,
            @RequestParam(required = false) String ciudad) {
        FiltroEventoDTO filtroEventoDTO = new FiltroEventoDTO(nombre, tipoEvento, ciudad);
        System.out.println("Filtro recibido: " + filtroEventoDTO);
        List<ItemEventoDTO> tipos = eventoServicio.filtrarEventos(filtroEventoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, tipos));
    }

}
