package co.edu.uniquindio.UniEventos.controllers;

import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.CrearAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.EditarAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.InfoAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.CuentaDTO.InfoCuentaDTO;
import co.edu.uniquindio.UniEventos.dto.CuponDTO.CrearCuponDTO;
import co.edu.uniquindio.UniEventos.dto.CuponDTO.EditarCuponDTO;
import co.edu.uniquindio.UniEventos.dto.EmailDTO.MensajeDTO;
import co.edu.uniquindio.UniEventos.dto.EventoDTO.CrearEventoDTO;
import co.edu.uniquindio.UniEventos.dto.EventoDTO.EditarEventoDTO;
import co.edu.uniquindio.UniEventos.dto.EventoDTO.ItemEventoDTO;
import co.edu.uniquindio.UniEventos.modelo.Cuenta;
import co.edu.uniquindio.UniEventos.servicios.interfaces.AlojamientoServicio;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CuentaServicio;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CuponServicio;
import co.edu.uniquindio.UniEventos.servicios.interfaces.EventoServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {

    private final CuentaServicio cuentaServicio;
    private final EventoServicio eventoServicio;
    private final CuponServicio cuponServicio;
    private final AlojamientoServicio alojamientoServicio;

    @GetMapping("/listar-cuentas")
    public ResponseEntity<MensajeDTO<List<Cuenta>>> listarCuentas() {
        List<Cuenta> accounts = cuentaServicio.listarCuentas();
        return ResponseEntity.ok(new MensajeDTO<>(false, accounts));
    }
    @PostMapping("/crear-evento")
    public ResponseEntity<MensajeDTO<String>> crearEvento(@Valid @RequestBody CrearEventoDTO crearEventoDTO) throws Exception {
        String eventId = eventoServicio.crearEvento(crearEventoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Evento creado exitosamente con ID: " + eventId));
    }
    @PutMapping("/editar-evento")
    public ResponseEntity<MensajeDTO<String>> editarEvento(@Valid @RequestBody EditarEventoDTO editarEventoDTO) throws Exception {
        String message = eventoServicio.editarEvento(editarEventoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, message));
    }

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

    @DeleteMapping("/eliminar-evento/{id}")
    public ResponseEntity<MensajeDTO<String>> deleteEvent(@PathVariable String id) throws Exception {
        String message = eventoServicio.eliminarEvento(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, message));
    }

    @PostMapping("/crear-alojamiento")
    public ResponseEntity<MensajeDTO<String>> crearAlojamiento(@Valid @RequestBody CrearAlojamientoDTO crearAlojamientoDTO) throws Exception {
        String alojamientoId = alojamientoServicio.crearAlojamiento(crearAlojamientoDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Alojamiento creado exitosamente con ID: " + alojamientoId));
    }

    @PutMapping("/editar-alojamiento/{id}")
    public ResponseEntity<MensajeDTO<String>> editarAlojamiento(@Valid @RequestBody EditarAlojamientoDTO editarAlojamientoDTO, @PathVariable String id) throws Exception {
        String message = alojamientoServicio.editarAlojamiento(editarAlojamientoDTO, id);
        return ResponseEntity.ok(new MensajeDTO<>(false, message));
    }

    @DeleteMapping("/eliminar-alojamiento/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarAlojamiento(@PathVariable String id) throws Exception {
        String message = alojamientoServicio.eliminarAlojamiento(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, message));
    }

    @GetMapping("/listar-alojamientos")
    public ResponseEntity<MensajeDTO<List<InfoAlojamientoDTO>>> listarAlojamientos() {
        List<InfoAlojamientoDTO> alojamientos = alojamientoServicio.listarAlojamientos();
        return ResponseEntity.ok(new MensajeDTO<>(false, alojamientos));
    }

    @PostMapping("/crear-cupon")
    public ResponseEntity<MensajeDTO<String>> crearCupon(@Valid @RequestBody CrearCuponDTO couponDTO) throws Exception {
        String couponId = cuponServicio.crearCupones(couponDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cupón creado exitosamente con ID: " + couponId));
    }

    @DeleteMapping("/desactivar-cupon/{couponId}")
    public ResponseEntity<MensajeDTO<String>> desactivarCupon(@PathVariable String couponId) throws Exception {
        cuponServicio.desactivarCupon(couponId);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cupón desactivado exitosamente."));
    }

    @DeleteMapping("/activar-cupon/{couponId}")
    public ResponseEntity<MensajeDTO<String>> activarCupon(@PathVariable String couponId) throws Exception {
        cuponServicio.activarCupon(couponId);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cupón activado exitosamente."));
    }

    @DeleteMapping("/eliminar-cupon/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarCupon(@PathVariable String id) throws Exception {
       cuponServicio.borrarCupon(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cupon eliminado correctamente"));
    }


    @PutMapping("/actualizar-cupon/{couponId}")
    public ResponseEntity<MensajeDTO<String>> actualizarCupon(@Valid @RequestBody EditarCuponDTO cuponDTO, @PathVariable String couponId) throws Exception {
        cuponServicio.actualizarCupon(cuponDTO, couponId);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cupón actualizado exitosamente."));
    }

}
