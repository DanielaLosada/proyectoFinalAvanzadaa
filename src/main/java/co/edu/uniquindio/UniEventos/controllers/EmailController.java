package co.edu.uniquindio.UniEventos.controllers;
import co.edu.uniquindio.UniEventos.dto.EmailDTO.EmailDTO;
import co.edu.uniquindio.UniEventos.dto.EmailDTO.MensajeDTO;
import co.edu.uniquindio.UniEventos.servicios.interfaces.EmailServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {
    private final EmailServicio emailServicio;

    // Enviar correo electrónico genérico
    @PostMapping("/enviar-correo")
    public ResponseEntity<MensajeDTO<String>> enviarCorreo(@Valid @RequestBody EmailDTO emailDTO) {
        try {
            emailServicio.enviarCorreo(emailDTO);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Correo enviado exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MensajeDTO<>(true, "Error al enviar el correo: " + e.getMessage()));
        }
    }

    // Enviar código de validación por correo electrónico
    @PostMapping("/enviar-codigo-activacion")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoActivacion(@RequestParam String email, @RequestParam String validationCode) {
        try {
            emailServicio.enviarCodigoActivacion(email, validationCode);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Correo con código de activacion enviado exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MensajeDTO<>(true, "Error al enviar el correo con código de activacion: " + e.getMessage()));
        }
    }

    // Enviar código de recuperación de contraseña
    @PostMapping("/enviar-codigo-password")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoPassword(@RequestParam String email, @RequestParam String codigoRecuperacion) {
        try {
            emailServicio.enviarCodigoPassword(email, codigoRecuperacion);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Correo con código de recuperación enviado exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MensajeDTO<>(true, "Error al enviar el correo con código de recuperación: " + e.getMessage()));
        }
    }

    // Enviar cupón de bienvenida por correo electrónico
    @PostMapping("/enviar-cupon-bienvenida")
    public ResponseEntity<MensajeDTO<String>> enviarCuponBienvenida(@RequestParam String email, @RequestParam String codigoCupon) {
        try {
            emailServicio.enviarCuponBienvenida(email, codigoCupon);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Correo con cupón de bienvenida enviado exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MensajeDTO<>(true, "Error al enviar el correo con cupón de bienvenida: " + e.getMessage()));
        }
    }

    //Enviar cupón de primera compra por correo electronico
    @PostMapping("/enviar-cupon-1compra")
    public ResponseEntity<MensajeDTO<String>> enviarCuponCompra(@RequestParam String email, @RequestParam String codigoCupon) {
        try {
            emailServicio.enviarCuponCompra(email, codigoCupon);
            return ResponseEntity.ok(new MensajeDTO<>(false, "Correo con cupón de primera compra enviado exitosamente."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new MensajeDTO<>(true, "Error al enviar el correo con cupón de primera compra: " + e.getMessage()));
        }
    }
}
