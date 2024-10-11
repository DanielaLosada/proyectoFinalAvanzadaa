package co.edu.uniquindio.UniEventos.modelo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Pago {
    @EqualsAndHashCode.Include
    private String id;
    private LocalDateTime fecha;
    private TipoPago tipoPago;
    private String moneda;
    private String detalleEstado;
    private EstadoPago estado;
    private String codigoAutorizacion;
    private Float valorTransaccion;
}
