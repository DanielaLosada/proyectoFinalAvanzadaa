package co.edu.uniquindio.UniEventos.modelo;

import lombok.*;

import java.time.LocalDateTime;
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class CodigoValidacion {
    private String codigo;
    private LocalDateTime fechaCreacion;
    public CodigoValidacion(String codigo, LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        this.codigo = codigo;
    }
    public boolean isExpired() {
        // Verifica si han pasado más de 15 minutos desde la creación
        return fechaCreacion.plusMinutes(15).isBefore(LocalDateTime.now());
    }
}
