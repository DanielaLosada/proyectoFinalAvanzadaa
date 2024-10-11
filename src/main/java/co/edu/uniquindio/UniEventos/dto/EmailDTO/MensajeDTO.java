package co.edu.uniquindio.UniEventos.dto.EmailDTO;

public record MensajeDTO<T>(
        boolean error,
        T respuesta) {
}
