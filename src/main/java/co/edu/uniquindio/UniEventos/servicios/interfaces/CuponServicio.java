package co.edu.uniquindio.UniEventos.servicios.interfaces;

import co.edu.uniquindio.UniEventos.dto.CuponDTO.CrearCuponDTO;
import co.edu.uniquindio.UniEventos.dto.CuponDTO.EditarCuponDTO;
import co.edu.uniquindio.UniEventos.dto.CuponDTO.InfoCuponDTO;
import co.edu.uniquindio.UniEventos.excepciones.CuponException;
import co.edu.uniquindio.UniEventos.modelo.Cupon;

import java.util.List;

public interface CuponServicio {
    String crearCupones(CrearCuponDTO cuponDTO);
    String actualizarCupon(EditarCuponDTO cuponDTO, String cuponId);
    void borrarCupon(String idCupon) throws Exception;
    void activarCupon(String idCupon) throws Exception;
    void desactivarCupon(String idCupon);
    boolean validarCupon(String codigo);

    InfoCuponDTO obtenerInformacionCupon(String id) throws CuponException;

    double redimirCupon(String idCupon, String idCliente);

    String generarCodigoCupon();

    List<Cupon> listarCupones();

    Cupon obtenerCupon(String codigo) throws CuponException;
}
