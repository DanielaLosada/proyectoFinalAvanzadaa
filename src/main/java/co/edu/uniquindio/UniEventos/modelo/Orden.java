package co.edu.uniquindio.UniEventos.modelo;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("ordenes")
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Orden {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String idUser;
    private LocalDateTime fecha;
    private List<DetalleOrden> items;
    private Pago pago;
    private String codigoCupon;
    private float total;
    private String codigoPasarela;
}
