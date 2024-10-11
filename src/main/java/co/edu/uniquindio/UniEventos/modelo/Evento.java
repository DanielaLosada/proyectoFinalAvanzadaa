package co.edu.uniquindio.UniEventos.modelo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("eventos")
@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
public class Evento {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String nombre;
    private String descripcion;
    private String direccion;
    private String ciudad;
    private LocalDateTime fecha;
    private TipoEvento tipo;
    private EstadoEvento estado;
    private String imagenPortada;
    private String imagenLocalidad;
    private List<Localidad> localidades;
    private List<Alojamiento> alojamientosCercanos;

    public Localidad obtenerLocalidad(String nombre) throws Exception {
        for(Localidad localidad: localidades){
            if(localidad.getNombre().equals(nombre)){
                return localidad;

            }else{
                throw new Exception("La localidad no existe");

            }
        }
        return null;
    }

}
