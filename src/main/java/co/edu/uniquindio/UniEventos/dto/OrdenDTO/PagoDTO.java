package co.edu.uniquindio.UniEventos.dto.OrdenDTO;

import co.edu.uniquindio.UniEventos.modelo.EstadoPago;
import co.edu.uniquindio.UniEventos.modelo.Pago;
import co.edu.uniquindio.UniEventos.modelo.TipoPago;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record PagoDTO(
        @NotNull String moneda,
        @NotNull TipoPago tipo,
        @NotNull String codigoAutorizacion,
        @NotNull LocalDateTime fecha,
        @NotNull float valorTransaccion,
        @NotNull EstadoPago estado
) {
    public Pago toEntity() {
        return Pago.builder()
                .moneda(this.moneda)
                .tipoPago(this.tipo)
                .codigoAutorizacion(this.codigoAutorizacion)
                .fecha(this.fecha)
                .valorTransaccion(this.valorTransaccion)
                .estado(this.estado)
                .build();
    }
}