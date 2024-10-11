package co.edu.uniquindio.UniEventos.modelo;

import lombok.*;
import org.bson.types.ObjectId;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class DetalleCarrito {
    private String id;
    private String idEvento;
    private String nombreEvento;
    private String nombreLocalidad;
    private double precio;
    private int cantidad;
    private double subtotal;
}
