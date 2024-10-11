package co.edu.uniquindio.UniEventos.controllers;
import co.edu.uniquindio.UniEventos.dto.EmailDTO.MensajeDTO;
import co.edu.uniquindio.UniEventos.dto.OrdenDTO.CrearOrdenDTO;
import co.edu.uniquindio.UniEventos.dto.OrdenDTO.DetalleOrdenDTO;
import co.edu.uniquindio.UniEventos.dto.OrdenDTO.InfoOrdenDTO;
import co.edu.uniquindio.UniEventos.dto.OrdenDTO.ItemOrdenDTO;
import co.edu.uniquindio.UniEventos.modelo.Orden;
import co.edu.uniquindio.UniEventos.servicios.interfaces.OrdenServicio;
import com.mercadopago.resources.preference.Preference;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orden")
public class OrdenController {
    private final OrdenServicio ordenServicio;

    @GetMapping
    public ResponseEntity<List<Orden>> getAll() throws Exception {
        return ResponseEntity.ok(ordenServicio.getAllOrders());
    }

    @GetMapping("/listar-ordenes/{id}")
    public ResponseEntity<List<Orden>> listarOrdenesCliente(@PathVariable ("id")String id) throws Exception {
        return ResponseEntity.ok(ordenServicio.listarOrdenesCliente(id));
    }

    @GetMapping("/info-orden/{id}")
    public ResponseEntity<MensajeDTO<InfoOrdenDTO>> obtenerInformacionOrden(@PathVariable ("id")String id) throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false, ordenServicio.obtenerInfoOrden(id)));
    }

    @PostMapping("/crear-orden")
    public ResponseEntity<MensajeDTO<String>> crearOrden(@Valid @RequestBody CrearOrdenDTO crearOrdenDTO) throws Exception {
        String message = ordenServicio.crearOrden(crearOrdenDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, message));
    }
    @DeleteMapping("/cancelar-orden/{id}")
    public ResponseEntity<MensajeDTO<String>> cancelarOrden(@PathVariable("id")String id) throws Exception {
        ordenServicio.cancelarOrden(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "La orden ha sido cancela exitosamente!"));
    }
    @GetMapping("/historial-orden/{id}")
    public ResponseEntity<MensajeDTO<List<DetalleOrdenDTO>>> obtenerHistorial(@PathVariable ("id")String id) throws Exception {
        return ResponseEntity.ok(new MensajeDTO<> (false, ordenServicio.obtenerHistorialOrdenes(id)));
    }

    @PostMapping("/realizar-pago")
    public ResponseEntity<MensajeDTO<Preference>> realizarPago(@RequestParam("idOrden") String idOrden) throws Exception{
        return ResponseEntity.ok().body(new MensajeDTO<>(false, ordenServicio.realizarPago(idOrden)));
    }

    @PostMapping("/notificacion-pago")
    public void recibirNotificacionMercadoPago(@RequestBody Map<String, Object> requestBody) {
        ordenServicio.recibirNotificacionMercadoPago(requestBody);
    }
}
