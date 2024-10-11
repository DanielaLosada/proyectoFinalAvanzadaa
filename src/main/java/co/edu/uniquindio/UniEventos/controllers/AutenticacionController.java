package co.edu.uniquindio.UniEventos.controllers;
import co.edu.uniquindio.UniEventos.dto.CuentaDTO.ActivarCuentaDTO;
import co.edu.uniquindio.UniEventos.dto.CuentaDTO.CambiarPasswordDTO;
import co.edu.uniquindio.UniEventos.dto.CuentaDTO.CrearCuentaDTO;
import co.edu.uniquindio.UniEventos.dto.CuentaDTO.LoginDTO;
import co.edu.uniquindio.UniEventos.dto.EmailDTO.MensajeDTO;
import co.edu.uniquindio.UniEventos.dto.TokenDTO;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CuentaServicio;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AutenticacionController {

    private final CuentaServicio cuentaServicio;

    @PostMapping("/cuenta/crear-cuenta")
    public ResponseEntity<MensajeDTO<String>> crearCuenta(@Valid @RequestBody CrearCuentaDTO cuenta) throws Exception {
        cuentaServicio.crearCuenta(cuenta);
        return ResponseEntity.ok(new MensajeDTO<>(false, "Cuenta creada exitosamente"));
    }

    @PostMapping("/cuenta/activar-cuenta")
    public ResponseEntity<MensajeDTO<String>> activateAccount(@Valid @RequestBody ActivarCuentaDTO activarCuentaDTO) throws Exception {
        cuentaServicio.activarCuenta(activarCuentaDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, "La cuenta se activó exitosamente"));
    }

    @PostMapping("/cuenta/iniciar-sesion")
    public ResponseEntity<MensajeDTO<TokenDTO>> iniciarSesion(@Valid @RequestBody LoginDTO loginDTO) throws Exception {
        TokenDTO token = cuentaServicio.iniciarSesion(loginDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, token));
    }
    @PutMapping("/cuenta/enviar-codigo-recuperacion/{email}")
    public ResponseEntity<MensajeDTO<String>> enviarCodigoRecuperacionPassword(@PathVariable String email) throws Exception {
        String cuentaId= cuentaServicio.enviarCodigoRecuperacionPassword(email);
        return ResponseEntity.ok(new MensajeDTO<>(false, cuentaId));
    }

    @PutMapping("/cambiar-password")
    public ResponseEntity<MensajeDTO<String>> cambiarPassword
            (@Valid @RequestBody CambiarPasswordDTO cambiarPasswordDTO) throws Exception {
        String cuentaId= cuentaServicio.cambiarPassword(cambiarPasswordDTO);
        return ResponseEntity.ok(new MensajeDTO<>(false, cuentaId));
    }

}
