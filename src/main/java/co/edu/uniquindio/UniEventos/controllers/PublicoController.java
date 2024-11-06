package co.edu.uniquindio.UniEventos.controllers;

import co.edu.uniquindio.UniEventos.dto.EmailDTO.MensajeDTO;
import co.edu.uniquindio.UniEventos.dto.EventoDTO.ItemEventoDTO;
import co.edu.uniquindio.UniEventos.modelo.Cuenta;
import co.edu.uniquindio.UniEventos.modelo.Evento;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CuentaServicio;
import co.edu.uniquindio.UniEventos.servicios.interfaces.EventoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
