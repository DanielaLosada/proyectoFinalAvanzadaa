package co.edu.uniquindio.UniEventos.modelo;

import lombok.*;
import org.bson.types.ObjectId;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Builder
public class Recomendacion {
    @EqualsAndHashCode.Include
    private String id;
    private ObjectId idUser;
    private List<Evento> eventos;
    private String motivo;
}
