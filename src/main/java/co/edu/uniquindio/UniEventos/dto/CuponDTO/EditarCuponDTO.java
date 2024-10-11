package co.edu.uniquindio.UniEventos.dto.CuponDTO;

import co.edu.uniquindio.UniEventos.modelo.EstadoCupon;
import co.edu.uniquindio.UniEventos.modelo.TipoCupon;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public record EditarCuponDTO(
        @NotBlank(message = "El código del cupón es obligatorio") @Length(max = 10, message = "El código del cupón no debe exceder los 10 caracteres") String codigo,
        @NotBlank(message = "El nombre del cupón es obligatorio") @Length(max = 60, message = "El nombre del cupón no debe exceder los 60 caracteres") String nombre,
        @Positive(message = "El descuento debe ser un número positivo") float descuento,
        @NotNull(message = "El tipo de cupón es obligatorio") TipoCupon tipo,
        @NotNull(message = "El estado del cupón es obligatorio") EstadoCupon estado,
        @Future(message = "La fecha de vencimiento debe estar en el futuro") @NotNull(message = "La fecha de vencimiento es obligatoria") LocalDateTime fechaVencimiento
) {
}
