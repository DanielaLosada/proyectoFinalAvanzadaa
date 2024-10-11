package co.edu.uniquindio.UniEventos.servicios.interfaces;

import co.edu.uniquindio.UniEventos.dto.CarritoDTO.ActualizarCarritoDTO;
import co.edu.uniquindio.UniEventos.dto.CarritoDTO.CrearCarritoDTO;
import co.edu.uniquindio.UniEventos.dto.CarritoDTO.ItemCarritoDTO;
import co.edu.uniquindio.UniEventos.excepciones.CarritoException;
import co.edu.uniquindio.UniEventos.modelo.Carrito;
import co.edu.uniquindio.UniEventos.modelo.DetalleCarrito;

import java.util.List;

public interface CarritoServicio {
    void añadirItemAlCarrito(String cuentaId, ItemCarritoDTO itemCarritoDTO) throws CarritoException;

    void removerItemCarrito(String cuentaId,  String eventId) throws CarritoException;

    Carrito crearCarrito(String idUser) throws Exception;

    void actualizarCarrito(String cuentaId, String eventoId, String localidadNombre, ActualizarCarritoDTO actualizarCarritoDTO) throws CarritoException;

    void limpiarCarrito(String cuentaId) throws CarritoException;

    List<DetalleCarrito> obtenerDetalleCarrito(String cuentaId) throws CarritoException;

//    Carrito obtenerCarritoPorUsuario(String idUsuario) throws CarritoException;
//
//    double calcularTotalCarrito(String idUsuario) throws CarritoException;

}
