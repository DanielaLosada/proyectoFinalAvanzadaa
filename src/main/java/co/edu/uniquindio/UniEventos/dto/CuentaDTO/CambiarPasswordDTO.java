package co.edu.uniquindio.UniEventos.dto.CuentaDTO;

public record CambiarPasswordDTO(
        String codigoVerificacion,
        String passwordNueva,
        String confirmacionPassword,
        String email
) {
}
