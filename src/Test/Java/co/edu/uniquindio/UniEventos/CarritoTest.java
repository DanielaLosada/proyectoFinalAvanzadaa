package co.edu.uniquindio.UniEventos;

import co.edu.uniquindio.UniEventos.dto.CarritoDTO.ItemCarritoDTO;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CarritoServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class CarritoTest {
    @Autowired
    private CarritoServicio carritoServicio;
    @Test
    public void agregarItemAlCarritoTest() throws Exception {
        String accountId = "670885b73255d028609d4242"; // ID de prueba
        String idEvent1 = "6708854b70201e74c36abcd9";
        String idEvent2 = "66f5c5a0de22e82833106d93";

        ItemCarritoDTO item1 = new ItemCarritoDTO(5, "General", idEvent1, LocalDateTime.now());
        ItemCarritoDTO item2 = new ItemCarritoDTO(6, "General", idEvent2, LocalDateTime.now());

        // Agregar ítems al carrito
        carritoServicio.añadirItemAlCarrito(accountId, item1);
    }

    /**
     * Método para remover un ítem del carrito.
     *
     * @throws Exception
     */
    @Test
    public void removerItemDelCarritoTest() throws Exception {
        String idAccount = "670885b73255d028609d4242";
        String idEvent = "6708854b70201e74c36abcd9";
        carritoServicio.removerItemCarrito(idAccount, idEvent);
    }

    /**
     * Método para limpiar el carrito.
     *
     * @throws Exception
     */
    @Test
    public void limpiarCarritoTest() throws Exception {
        String idAccount = "670885b73255d028609d4242";
        carritoServicio.limpiarCarrito(idAccount);
    }

}
