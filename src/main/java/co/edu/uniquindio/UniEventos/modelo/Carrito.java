package co.edu.uniquindio.UniEventos.modelo;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Document("carritos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Carrito {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String idUser;
    private LocalDateTime fecha;
    private List<DetalleCarrito> items;
    private double total;

    public void calcularTotal() {
        if (items != null) {
            this.total = items.stream()
                    .mapToDouble(item -> item.getPrecio() * item.getCantidad())
                    .sum();
        } else {
            this.total = 0;
        }
    }
}
