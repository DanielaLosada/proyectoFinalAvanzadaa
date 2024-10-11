package co.edu.uniquindio.UniEventos.dto.CuentaDTO;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record EditarCuentaDTO(
        @NotBlank @Length( max = 50) String nombre,
        @NotBlank @Length( max = 10) String telefono,
        @NotBlank @Length( max = 50) String direccion
) {
}
