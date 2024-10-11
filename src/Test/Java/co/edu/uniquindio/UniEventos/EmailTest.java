package co.edu.uniquindio.UniEventos;

import co.edu.uniquindio.UniEventos.dto.EmailDTO.EmailDTO;
import co.edu.uniquindio.UniEventos.servicios.interfaces.EmailServicio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailTest {
    @Autowired
    EmailServicio emailServicio;

    @Test
    public void enviarTest() throws Exception{

        EmailDTO emailDTO = new EmailDTO(
                "danielai.losadar@uqvirtual.edu.co",
                "Test",
                "Se hace prueba de que se envie correctamente el correo"
        );
        emailServicio.enviarCorreo(emailDTO);
        Assertions.assertNotNull("asunto",emailDTO.asunto());
    }
}
