package co.edu.uniquindio.UniEventos.servicios.implementaciones;

import co.edu.uniquindio.UniEventos.dto.CuponDTO.CrearCuponDTO;
import co.edu.uniquindio.UniEventos.dto.CuponDTO.EditarCuponDTO;
import co.edu.uniquindio.UniEventos.dto.CuponDTO.InfoCuponDTO;
import co.edu.uniquindio.UniEventos.excepciones.CuponException;
import co.edu.uniquindio.UniEventos.excepciones.OrdenException;
import co.edu.uniquindio.UniEventos.modelo.Cupon;
import co.edu.uniquindio.UniEventos.modelo.EstadoCupon;
import co.edu.uniquindio.UniEventos.modelo.Orden;
import co.edu.uniquindio.UniEventos.modelo.TipoCupon;
import co.edu.uniquindio.UniEventos.repositorios.CuponRepo;
import co.edu.uniquindio.UniEventos.repositorios.OrdenRepo;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CuponServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CuponServicioImpl implements CuponServicio {

    private final CuponRepo cuponRepo;
    private final OrdenRepo ordenRepo;
    @Override
    public String crearCupones(CrearCuponDTO cuponDTO) {
        // Verificar si el código del cupón ya existe
        Optional<Cupon> existeCupon = cuponRepo.findByCodigo(cuponDTO.codigo());
        if (existeCupon.isPresent()) {
            throw new CuponException("Ya existe un cupón con el código: " + cuponDTO.codigo());
        }
        // Comprobar la fecha de expiración
        if (cuponDTO.fechaVencimiento() != null && cuponDTO.fechaVencimiento().isBefore(LocalDateTime.now())) {
            throw new CuponException("La fecha de expiración no puede estar en el pasado.");
        }
        // Validar fechas de inicio y expiración
        if (cuponDTO.fechaApertura() != null && cuponDTO.fechaVencimiento() != null) {
            if (cuponDTO.fechaApertura().isAfter(cuponDTO.fechaVencimiento())) {
                throw new CuponException("La fecha de inicio no puede ser posterior a la fecha de fin.");
            }
        }
        Cupon nuevoCupon = new Cupon();
        nuevoCupon.setNombre(cuponDTO.nombre());
        nuevoCupon.setCodigo(generarCodigoCupon());
        nuevoCupon.setDescuento(cuponDTO.descuento());
        nuevoCupon.setFechaVencimiento(cuponDTO.fechaVencimiento());
        nuevoCupon.setEstado(cuponDTO.estado());
        nuevoCupon.setTipo(cuponDTO.tipo());
        // Asignar la fecha de inicio si se especificó
        if (cuponDTO.fechaApertura() != null) {
            nuevoCupon.setFechaApertura(cuponDTO.fechaApertura());
        }
        Cupon cuponCreado = cuponRepo.save(nuevoCupon);
        return cuponCreado.getId();
    }

    @Override
    public String actualizarCupon(EditarCuponDTO cuponDTO, String cuponId) {
        if (cuponDTO == null || cuponId == null || cuponId.isEmpty()) {
            throw new CuponException("Los datos del cupon o el id no pueden ser nulos");
        }
        Cupon cuponExistente = cuponRepo.findById(cuponId)
                .orElseThrow(() -> new CuponException("Cupon no encontrado"));
        cuponExistente.setNombre(cuponDTO.nombre());
        cuponExistente.setCodigo(cuponDTO.codigo());
        cuponExistente.setEstado(cuponDTO.estado());
        cuponExistente.setTipo(cuponDTO.tipo());
        cuponExistente.setDescuento(cuponDTO.descuento());
        cuponExistente.setFechaVencimiento(cuponDTO.fechaVencimiento());
        cuponRepo.save(cuponExistente);
        return "Cupon actualizado con éxito";
    }

    @Override
    public void borrarCupon(String idCupon) throws Exception {
        Cupon coupon = cuponRepo.findById(idCupon)
                .orElseThrow(() -> new CuponException("El cupón no existe"));
        coupon.setEstado(EstadoCupon.ELIMINADO);
        cuponRepo.save(coupon);
    }

    @Override
    public void activarCupon(String idCupon) throws Exception {
        Cupon cupon = cuponRepo.findById(idCupon)
                .orElseThrow(() -> new CuponException("El cupón no existe"));
        cupon.setEstado(EstadoCupon.DISPONIBLE);
        cuponRepo.save(cupon);
    }

    @Override
    public void desactivarCupon(String idCupon) {
        Cupon cupon = cuponRepo.findById(idCupon)
                .orElseThrow(() -> new CuponException("El cupón no existe"));
        cupon.setEstado(EstadoCupon.NO_DISPONIBLE);
        cuponRepo.save(cupon);
    }

    @Override
    public InfoCuponDTO obtenerInformacionCupon(String id) throws CuponException {
        // Validar que el ID no sea nulo o vacío
        if (id == null || id.isEmpty()) {
            throw new CuponException("El ID del cupon no puede ser nulo o vacío");
        }
        Cupon cuponABuscar = cuponRepo.findById(id)
                .orElseThrow(() -> new CuponException("Cupon no encontrado"));
        return new InfoCuponDTO(
                cuponABuscar.getNombre(),
                cuponABuscar.getCodigo(),
                cuponABuscar.getDescuento(),
                cuponABuscar.getFechaApertura(),
                cuponABuscar.getFechaVencimiento(),
                cuponABuscar.getTipo(),
                cuponABuscar.getEstado()
        );
    }

    @Override
    public double redimirCupon(String codigoCupon, String ordenId) {
        // Buscar el cupón por su código
        Cupon cupon = cuponRepo.findCuponByCodigo(codigoCupon);
        if (cupon == null) {
            throw new CuponException("El cupón no existe.");
        }
        // Validar si el cupón es válido, no expirado y no utilizado
        if (cupon.getFechaVencimiento().isBefore(LocalDateTime.now())) {
            throw new CuponException("El cupón ha expirado.");
        }
        // Verificar el tipo de cupón (UNICO o MULTIPLE)
        if (cupon.getTipo() == TipoCupon.UNICO && cupon.getEstado() == EstadoCupon.NO_DISPONIBLE) {
            throw new CuponException("El cupón ya ha sido utilizado.");
        }
        // Buscar la orden por su ID
        Orden orden = ordenRepo.findById(ordenId)
                .orElseThrow(() -> new OrdenException("La orden no existe"));
        // Aplicar el cupón a la orden
        double descuento = aplicarDescuentoOrden(orden, cupon);
        // Marcar el cupón como usado si es UNICO
        if (cupon.getTipo() == TipoCupon.UNICO) {
            cupon.setEstado(EstadoCupon.NO_DISPONIBLE);
            cuponRepo.save(cupon);
        }
        // Guardar la orden actualizada
        ordenRepo.save(orden);
        // Retornar el valor del descuento aplicado
        return descuento;
    }

    private double aplicarDescuentoOrden(Orden order, Cupon cupon) {
        // Lógica para calcular el descuento basado en el tipo de cupón
        double  discountPercentage = Integer.parseInt(String.valueOf(cupon.getDescuento()))/ 100.0;
        // Calcular el monto del descuento
        double discountAmount = order.getTotal() * discountPercentage;
        // Actualizar el total de la orden
        order.setTotal((float) (order.getTotal() - discountAmount));
        return discountAmount;
    }

    @Override
    public boolean validarCupon(String codigo) {
        Cupon cupon = cuponRepo.findCuponByCodigo(codigo);
        if(cupon == null || cupon.getEstado() != EstadoCupon.DISPONIBLE){
            throw new CuponException("El cupon no existe o no esta activo");
        }
        return true;
    }

    @Override
    public String generarCodigoCupon() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        int tam = 10;
        SecureRandom random = new SecureRandom();
        StringBuilder codigo= new StringBuilder(tam);
        for (int i = 0; i < tam; i++) {
            int index = random.nextInt(caracteres.length());
            codigo.append(caracteres.charAt(index));
        }
        return codigo.toString();
    }

    @Override
    public List<Cupon> listarCupones() {
        return cuponRepo.findAll();
    }

    @Override
    public Cupon obtenerCupon(String codigo) throws CuponException {
        Optional<Cupon> cupon = cuponRepo.findByCodigo(codigo);
        if (cupon.isEmpty()) {
            throw new CuponException("El cupon con ese codigo no existe");
        }
        return cupon.get(); // Devuelve el cupón si está presente
    }
}
