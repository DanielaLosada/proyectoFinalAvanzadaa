package co.edu.uniquindio.UniEventos.modelo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("cupones")
public class Cupon {
    @Id
    private String id;
    private String nombre;
    private String codigo;
    private TipoCupon tipo;
    private EstadoCupon estado;
    private Float descuento;
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaVencimiento;
}
