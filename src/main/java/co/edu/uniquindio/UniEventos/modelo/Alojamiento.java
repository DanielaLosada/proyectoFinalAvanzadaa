package co.edu.uniquindio.UniEventos.modelo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document("alojamientos")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Alojamiento implements Serializable {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String nombre;
    private String ciudad;
    private float precioNoche;
    private float calificacion;
    private float distanciaEvento;
    private TipoAlojamiento tipo;
    private EstadoAlojamiento estado;
}
