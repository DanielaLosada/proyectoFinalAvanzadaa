package co.edu.uniquindio.UniEventos;

import co.edu.uniquindio.UniEventos.dto.OrdenDTO.CrearOrdenDTO;
import co.edu.uniquindio.UniEventos.dto.OrdenDTO.PagoDTO;
import co.edu.uniquindio.UniEventos.modelo.*;
import co.edu.uniquindio.UniEventos.repositorios.EventoRepo;
import co.edu.uniquindio.UniEventos.servicios.interfaces.OrdenServicio;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OrdenTest {
    @Autowired
    private OrdenServicio ordenServicio;
    @Autowired
    EventoRepo eventoRepo;


    @Test
    public void crearOrdenTest() throws Exception {
        // Obtener el evento desde la base de datos para asegurar que los datos estén actualizados
        ObjectId idEvento = new ObjectId("6708854b70201e74c36abcd9");

        // Buscar el evento por su ID en la base de datos
        Optional<Evento> eventoOptional = eventoRepo.findById(idEvento.toHexString());

        // Verificar que el evento exista antes de continuar
        assertTrue(eventoOptional.isPresent(), "El evento no se encontró en la base de datos");
        Evento evento = eventoOptional.get();

        List<DetalleOrden> detallesOrden = new ArrayList<>();

        // Obtener el precio de la localidad desde el evento
        Localidad localidad = evento.getLocalidades().stream()
                .filter(loc -> loc.getNombre().equals("General"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Localidad no encontrada"));

        // Crear detalles de la orden utilizando el precio de la localidad obtenida del evento
        DetalleOrden detalleOrden1 = DetalleOrden.builder()
                .idEvento(idEvento.toString())
                .precio(localidad.getPrecio()) // Usar el precio obtenido del evento
                .nombreLocalidad("General")
                .cantidad(2)
                .build();

        detallesOrden.add(detalleOrden1);

        // Calcular el total basado en los detalles de la orden
        float total = (float) detallesOrden.stream()
                .mapToDouble(detalle -> detalle.getPrecio() * detalle.getCantidad())
                .sum();


        PagoDTO pagoDTO = new PagoDTO(
                "COP",
                TipoPago.TARJETA_CREDITO,
                "",
                LocalDateTime.now(),
                150000,
                EstadoPago.EN_PROCESO
        );

        CrearOrdenDTO ordenDTO = new CrearOrdenDTO(
                "670885b73255d028609d4242",
                LocalDateTime.now(),
                "",
                pagoDTO,
                total,
                detallesOrden,// Usar el total calculado
                null
        );

        // Ejecutar la creación de la orden
        String ordenGuardada = ordenServicio.crearOrden(ordenDTO);

    }

    @Test
    public void eliminarOrdenTest() throws Exception {
        String idOrden = "670886f47ae622103a2bcb06";
        ordenServicio.cancelarOrden(idOrden);
    }

    @Test
    public void obtenerOrdenesPorUsuarioTest() throws Exception {
        String idUsuario = "670885b73255d028609d4242";
        System.out.println(ordenServicio.obtenerHistorialOrdenes(idUsuario));
    }

}
