package co.edu.uniquindio.UniEventos.dto.EmailDTO;

import org.hibernate.validator.constraints.Length;

public record EmailDTO(
        @Length(max = 50) String destinatario,
        @Length(max = 50) String asunto,
        @Length(max = 500) String cuerpo) {
}
