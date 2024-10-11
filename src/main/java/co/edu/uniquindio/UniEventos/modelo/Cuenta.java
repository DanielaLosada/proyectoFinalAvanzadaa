package co.edu.uniquindio.UniEventos.modelo;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "cuentas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@ToString
public class Cuenta {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private Usuario user;
    private String password;
    private String email;
    private Rol rol;
    private EstadoCuenta estado;
    private LocalDateTime fechaRegistro;
    private CodigoValidacion codigoValidacionRegistro;
    private CodigoValidacion codigoValidacionPassword;
    private List<String> busquedasEventos;

    @Builder
    public Cuenta(Usuario user, String email, String password, CodigoValidacion codValidacionPassword, CodigoValidacion codValidacionRegistro, LocalDateTime fechaRegistro, Rol rol, EstadoCuenta estado) {
        this.user = user;
        this.email = email;
        this.password = password;
        this.codigoValidacionPassword = codValidacionPassword;
        this.codigoValidacionRegistro = codValidacionRegistro;
        this.fechaRegistro = fechaRegistro;
        this.rol = rol;
        this.estado = estado;
    }
}
