package co.edu.uniquindio.UniEventos.controllers;
import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.InfoAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.CarritoDTO.ActualizarCarritoDTO;
import co.edu.uniquindio.UniEventos.dto.CarritoDTO.ItemCarritoDTO;
import co.edu.uniquindio.UniEventos.dto.CuentaDTO.EditarCuentaDTO;
import co.edu.uniquindio.UniEventos.dto.CuentaDTO.InfoCuentaDTO;
import co.edu.uniquindio.UniEventos.dto.EmailDTO.MensajeDTO;
import co.edu.uniquindio.UniEventos.dto.OrdenDTO.InfoOrdenDTO;
import co.edu.uniquindio.UniEventos.excepciones.CarritoException;
import co.edu.uniquindio.UniEventos.servicios.interfaces.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cliente")
public class ClienteController {
    private final CuentaServicio cuentaServicio;
    private final CarritoServicio carritoServicio;
    private final CuponServicio cuponServicio;
    private final AlojamientoServicio alojamientoServico;
    private final OrdenServicio ordenServicio;

    @GetMapping("/cuenta/obtener-info/{id}")
    public InfoCuentaDTO obtenerInformacionCuenta(@PathVariable String id) throws Exception {
        return cuentaServicio.obtenerInformacionCuenta(id);
    }

    @PutMapping("/cuenta/editar-cuenta/{id}")
    public ResponseEntity<MensajeDTO<String>> editarCuenta(@Valid @RequestBody EditarCuentaDTO cuenta, @PathVariable String id) throws Exception{
        cuentaServicio.editarCuenta(cuenta, id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta editada exitosamente"));
    }

    @DeleteMapping("/cuenta/eliminar-cuenta/{id}")
    public ResponseEntity<MensajeDTO<String>> eliminarCuenta(@PathVariable String id) throws Exception{
        cuentaServicio.eliminarCuenta(id);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta eliminada exitosamente"));
    }

//    @PostMapping("/email/enviar-codigo/{correo}")
//    public ResponseEntity<MessageDTO<String>>  sendPasswordRecoveryCode(@PathVariable String correo) throws Exception{
//        accountService.sendPasswordRecoveryCode(correo);
//        return ResponseEntity.ok(new MessageDTO<>(true, "Se envio el codigo exitosamente"));
//    }
//
//    @PutMapping("/cuenta/cambiar-contrsena/{correo}")
//    public ResponseEntity<MessageDTO<String>> changePassword(@Valid @RequestBody changePasswordDTO changePasswordDTO, @PathVariable String correo) throws Exception{
//        accountService.changePassword(changePasswordDTO, correo);
//        return ResponseEntity.ok(new MessageDTO<>(true, "Se cambio la contraseña exitosamente"));
//    }

    //carrito

    @PostMapping("/carrito/agregar-item/{cuentaId}")
    public ResponseEntity<MensajeDTO<String>> añadirItemCarrito(
            @Valid @RequestBody ItemCarritoDTO cartDetailDTO,
            @PathVariable String cuentaId) throws Exception {
        try {
            // Llamada al servicio para agregar el ítem al carrito
            carritoServicio.añadirItemAlCarrito(cuentaId, cartDetailDTO);
            // Retornar una respuesta exitosa
            return ResponseEntity.ok(new MensajeDTO<>(false, "Ítem agregado exitosamente al carrito"));
        } catch (Exception e) {
            // Manejar cualquier excepción que ocurra durante la operación
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MensajeDTO<>(true, "Error al agregar el ítem: " + e.getMessage()));
        }
    }


    @PostMapping("/carrito/limpiar-carrito/{cuentaId}")
    public ResponseEntity<MensajeDTO<String>> limpiarCarrito(@PathVariable String cuentaId) throws Exception {
        try {
            carritoServicio.limpiarCarrito(cuentaId);
            return ResponseEntity.ok(new MensajeDTO<>(true, "!Se limpió correctamente el carro!"));
        } catch (CarritoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    @PostMapping("/carrito/eliminar-item/{accountId}/{eventId}")
    public ResponseEntity<MensajeDTO<String>> removeItemFromCart(@PathVariable String accountId,@PathVariable String eventId) throws Exception {
        try {
            carritoServicio.removerItemCarrito(accountId, eventId);
            return ResponseEntity.ok(new MensajeDTO<>(true, "!se retiro correctamente el item!"));
        } catch (CarritoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MensajeDTO<>(false, e.getMessage()));
        }
    }

    @PostMapping("/realizar-pago/{idOrden}")
    public ResponseEntity<MensajeDTO<String>> realizarPago(@PathVariable String idOrden)throws Exception {
        ordenServicio.realizarPago(idOrden);
        return ResponseEntity.ok(new MensajeDTO<>(false,"pago exitoso"));
    }
    // Coupon

    // Aplicar un cupón a un pedido
    @PostMapping("/cupon/aplicar-cupon")
    public ResponseEntity<MensajeDTO<Double>> aplicarCupon(@RequestParam String code, @RequestParam String orderId) throws Exception {
        double discount = cuponServicio.redimirCupon(code, orderId);
        return ResponseEntity.ok(new MensajeDTO<>(false, discount));
    }

    @GetMapping("/info-alojamiento/{id}")
    public ResponseEntity<MensajeDTO<InfoAlojamientoDTO>> ObtenerInformacionAlojamiento(@PathVariable ("id")String id) throws Exception {
        return ResponseEntity.ok(new MensajeDTO<>(false, alojamientoServico.obtenerInformacionAlojamiento(id)));
    }


}
