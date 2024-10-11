package co.edu.uniquindio.UniEventos;

import co.edu.uniquindio.UniEventos.dto.CuponDTO.CrearCuponDTO;
import co.edu.uniquindio.UniEventos.dto.CuponDTO.EditarCuponDTO;
import co.edu.uniquindio.UniEventos.dto.CuponDTO.InfoCuponDTO;
import co.edu.uniquindio.UniEventos.excepciones.CuponException;
import co.edu.uniquindio.UniEventos.modelo.Cupon;
import co.edu.uniquindio.UniEventos.modelo.EstadoCupon;
import co.edu.uniquindio.UniEventos.modelo.TipoCupon;
import co.edu.uniquindio.UniEventos.repositorios.CuponRepo;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CuponServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CuponTest {
    @Autowired
    private CuponServicio cuponServicio;
    private CuponRepo cuponRepo;

    @Test
    public void crearCuponTest() throws Exception {
        // Llamar al servicio para crear el cupón
        // Crear un nuevo objeto CouponDTO con nombre, código, descuento, fecha de expiración, estado y tipo
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(30); // La fecha de expiración es en 30 días
        CrearCuponDTO couponDTO = new CrearCuponDTO(
                cuponServicio.generarCodigoCupon(),
                "Emprendimiento",     // Nombre del cupón
                10,
                TipoCupon.INDIVIDUAL,
                EstadoCupon.NO_DISPONIBLE,// Descuento del cupón
                expirationDate,          // Fecha de expiración, // Estado del cupón// Tipo de cupón
                LocalDateTime.now()           // Se usa expirationDate como endDate
        );
        // Llamar al servicio para crear el cupón
        cuponServicio.crearCupones(couponDTO);
    }

    @Test
    public void actualizarCuponTest() throws CuponException {
        // Datos de prueba
        String cuponId = "67080846e39ed81c66949dae";
        EditarCuponDTO cuponDTO = new EditarCuponDTO(cuponServicio.generarCodigoCupon(), "CODIGO123", 19, TipoCupon.UNICO, EstadoCupon.DISPONIBLE, LocalDateTime.now().plusDays(15));
        String resultado = cuponServicio.actualizarCupon(cuponDTO, cuponId);
        // Verificar resultados
        assertEquals("Cupon actualizado con éxito", resultado);
    }
    @Test
    public void desactivarCuponTest() throws Exception {
        String couponId = "67080846e39ed81c66949dae"; // ID del cupón a desactivar
        // Llamar al servicio para desactivar el cupón
        cuponServicio.desactivarCupon(couponId);
    }
    // Prueba para activar un cupón
    // Se activa un cupón por su ID utilizando el servicio
    @Test
    public void activarCuponTest() throws Exception {
        String couponId = "67080846e39ed81c66949dae"; // ID del cupón a activar
        // Llamar al servicio para activar el cupón
        cuponServicio.activarCupon(couponId);
    }

    @Test
    public void borrarCuponTest() throws Exception {
        // Datos de prueba
        String cuponId = "67080846e39ed81c66949dae";
        cuponServicio.borrarCupon(cuponId);
    }

    @Test
    public void obtenerInformacionCuponTest() throws CuponException {
        // Datos de prueba
        String cuponId = "67080846e39ed81c66949dae";
        // Llamar al método
        InfoCuponDTO infoCupon = cuponServicio.obtenerInformacionCupon(cuponId);

        // Verificar resultados
        assertEquals("CODIGO123", infoCupon.nombre());
        assertEquals("GSBqBnVZ9C", infoCupon.codigo());
    }

    @Test
    public void obtenerCuponTest() throws CuponException {
        String codigoCupon = "GSBqBnVZ9C";
        Cupon cuponObtenido = cuponServicio.obtenerCupon(codigoCupon);
        assertNotNull(cuponObtenido);
        assertEquals(codigoCupon, cuponObtenido.getCodigo());
    }

    @Test
    public void listarCuponesTest() {
        List<Cupon> cupones = cuponServicio.listarCupones();
        // Verificar resultados
        assertEquals(1, cupones.size());
    }




}
