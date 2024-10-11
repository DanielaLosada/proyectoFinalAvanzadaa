package co.edu.uniquindio.UniEventos;

import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.CrearAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.EditarAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.InfoAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.CuponDTO.CrearCuponDTO;
import co.edu.uniquindio.UniEventos.dto.CuponDTO.EditarCuponDTO;
import co.edu.uniquindio.UniEventos.dto.CuponDTO.InfoCuponDTO;
import co.edu.uniquindio.UniEventos.excepciones.CuponException;
import co.edu.uniquindio.UniEventos.modelo.*;
import co.edu.uniquindio.UniEventos.servicios.interfaces.AlojamientoServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AlojamientoTest {
    @Autowired
    private AlojamientoServicio alojamientoServicio;
    @Test
    public void crearAlojamientoTest() throws Exception {
        CrearAlojamientoDTO alojamientoDTO = new CrearAlojamientoDTO(
                "Hotel Real"  ,
                "Armenia",
                50000,
                60,
                45,
                TipoAlojamiento.HOTEL,
                EstadoAlojamiento.ACTIVO// Se usa expirationDate como endDate
        );
        // Llamar al servicio para crear el cupón
        alojamientoServicio.crearAlojamiento(alojamientoDTO);
    }

    @Test
    public void actualizarAlojamientoTest() throws Exception {
        String alojamientoID = "670814940a11b438c99309e3";
        EditarAlojamientoDTO alojamientoDTO = new EditarAlojamientoDTO(
                "Hotel Villa Real",
                80000,
                80,
                TipoAlojamiento.HOTEL,
                EstadoAlojamiento.ACTIVO);
        String resultado = alojamientoServicio.editarAlojamiento(alojamientoDTO, alojamientoID);
        // Verificar resultados
        assertEquals("El alojamiento ha sido actualizado correctamente", resultado);
    }

    @Test
    public void borrarAlojamientoTest() throws Exception {
        // Datos de prueba
        String alojamientoId = "670814940a11b438c99309e3";
        alojamientoServicio.eliminarAlojamiento(alojamientoId);
    }

    @Test
    public void obtenerInformacionAlojamientoTest() throws Exception {
        String alojamientoId = "670814940a11b438c99309e3";
        InfoAlojamientoDTO infoAlojamientoDTO = alojamientoServicio.obtenerInformacionAlojamiento(alojamientoId);
        assertEquals("Hotel Villa Real", infoAlojamientoDTO.nombre());
    }

    @Test
    public void listarALojamientosTest() {
        List<InfoAlojamientoDTO> alojamientos = alojamientoServicio.listarAlojamientos();
        // Verificar resultados
        assertEquals(1, alojamientos.size());
    }
}
