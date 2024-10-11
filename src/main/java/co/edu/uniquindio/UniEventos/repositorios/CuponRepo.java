package co.edu.uniquindio.UniEventos.repositorios;

import co.edu.uniquindio.UniEventos.modelo.Cupon;
import co.edu.uniquindio.UniEventos.modelo.EstadoCupon;
import co.edu.uniquindio.UniEventos.modelo.TipoCupon;
import co.edu.uniquindio.UniEventos.modelo.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CuponRepo extends MongoRepository<Cupon, String> {
    Boolean existsByCodigo(String codigo);

    Optional<Cupon> findByCodigo(String codigo);
    @Query("{ 'codigo': ?0 }")
    Cupon findCuponByCodigo(String code);
    @Query("{ 'estado': 'DISPONIBLE' }")
    List<Cupon> encontrarCuponesDisponible();

//    List<Cupon> findByNombreContainingIgnoreCase(String nombre);
//    List<Cupon> findByFechaVencimientoAfter(LocalDateTime fechaVencimiento);
//    List<Cupon> findByFechaAperturaAfter(LocalDateTime fechaApertura);
//    List<Cupon> findByDescuento(Float descuento);
//    List<Cupon> findByTipo(TipoCupon tipo);
//    List<Cupon> findByEstado(EstadoCupon estado);
}
