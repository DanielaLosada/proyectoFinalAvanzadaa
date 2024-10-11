package co.edu.uniquindio.UniEventos;

import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.CrearAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.EventoDTO.*;
import co.edu.uniquindio.UniEventos.modelo.*;
import co.edu.uniquindio.UniEventos.repositorios.EventoRepo;
import co.edu.uniquindio.UniEventos.servicios.interfaces.EventoServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EventoTest {

    @Autowired
    private EventoServicio eventoServicio;

    @Autowired
    private EventoRepo eventoRepo;

    // Prueba para crear un evento
    @Test
    public void crearEventoTest() throws Exception {
        // Crear DTO para el nuevo evento con datos de prueba
        CrearEventoDTO crearEventoDTO = new CrearEventoDTO(
                "Concierto de Rock",
                "Un increíble concierto de rock en vivo",
                "Calle 123 #45-67",
                "Bogotá",
                LocalDateTime.of(2024, 12, 5, 19, 30),
                TipoEvento.CONCIERTO,
                "imagen_concierto.jpg",
                "imagen_localidad.png",// fecha
                List.of(                                // localidades
                        new LocalidadDTO("Platinum", 500, 300000),   // Localidad Platinum, 500 asientos, 300,000 COP cada entrada
                        new LocalidadDTO("General", 1000, 150000)),
                        List.of(new CrearAlojamientoDTO("Hotel Real", "Armenia", 1500000, 56, 800, TipoAlojamiento.HOTEL, EstadoAlojamiento.ACTIVO))

        );
        // Llamar al método de servicio para crear el evento
        String eventId = eventoServicio.crearEvento(crearEventoDTO);
        // Verificar que el ID generado del evento no sea nulo
        assertNotNull(eventId);
    }

    @Test
    public void ActualizarEventoTest() throws Exception {
        // Crear DTO para editar un evento existente con nuevos datos
        EditarEventoDTO editarEventoDTO = new EditarEventoDTO(
                "67087d04c1e8ee0d9909ba2e",  // id (obligatorio, no puede estar en blanco)
                "Feria Internacional de Tecnología",  // nombre (obligatorio, no puede estar en blanco y máximo 150 caracteres)
                "Un evento que reúne a los mejores exponentes de la tecnología a nivel mundial.",  // descripción (obligatoria, máximo 800 caracteres)
                "Av. Tecnológica 123, Edificio Innovación, Piso 5",  // dirección (obligatoria, máximo 350 caracteres)
                "Medellín",  // ciudad (obligatoria, máximo 60 caracteres)
                LocalDateTime.of(2024, 11, 20, 9, 0),  // fecha (obligatoria, debe ser una fecha válida)
                TipoEvento.TEATRO,  // tipo (obligatorio, enum definido para tipos de eventos)
                EstadoEvento.ACTIVO,  // estado (obligatorio, enum para el estado del evento)
                "https://example.com/imagen_poster.jpg",  // imagenPoster (URL válida, opcional pero si está presente debe ser una URL)
                "https://example.com/imagen_localidades.png",  // imagenLocalidades (URL válida, opcional pero si está presente debe ser una URL)
                // Lista de localidades (obligatoria)
                List.of(
                        new LocalidadDTO("VIP", 200, 500000),  // Localidad VIP, 200 asientos, 500,000 COP por entrada
                        new LocalidadDTO("General", 800, 200000)  // Localidad General, 800 asientos, 200,000 COP por entrada
                )
        );
        // Llamar al servicio para actualizar el evento
        String re = eventoServicio.editarEvento(editarEventoDTO);
    }

    // Prueba para eliminar un evento
    @Test
    public void EliminarEventoTest() throws Exception {
        // ID del evento que se asume ya existe en la base de datos
        String eventId = "67087d04c1e8ee0d9909ba2e";
        // Llamar al método del servicio para eliminar el evento
        String result = eventoServicio.eliminarEvento(eventId);
    }

    // Prueba para obtener la información de un evento
    @Test
    public void obtenerInformacionEventoTest() throws Exception {
        // ID del evento que se asume ya existe en la base de datos
        String eventId = "67087d04c1e8ee0d9909ba2e";
        // Llamar al método de servicio para obtener la información del evento
        InfoEventoDTO informacionEventoDTO = eventoServicio.obtenerInformacionEvento(eventId);
        // Verificar que el DTO con la información del evento no sea nulo
        assertNotNull(informacionEventoDTO);
    }

    // Prueba para listar todos los eventos
    @Test
    public void ListEvents() throws Exception {
        // Imprimir la lista de todos los eventos
        System.out.println(eventoServicio.listarEventos());
    }

    @Test
    public void obtenerEventoTest() throws Exception {
        String idEvent ="67087d04c1e8ee0d9909ba2e";
        eventoServicio.obtenerEvento(idEvent);
    }

    @Test
    public void eventFilter(){
        FiltroEventoDTO filter = new FiltroEventoDTO(
                "Feria Internacional de Tecnología",
                TipoEvento.TEATRO,
                "Armenia"
        );

        eventoServicio.filtrarEventos(filter);
    }
}
