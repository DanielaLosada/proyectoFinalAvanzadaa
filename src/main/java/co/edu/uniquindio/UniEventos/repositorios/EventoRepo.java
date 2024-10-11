package co.edu.uniquindio.UniEventos.repositorios;

import co.edu.uniquindio.UniEventos.modelo.EstadoEvento;
import co.edu.uniquindio.UniEventos.modelo.Evento;
import co.edu.uniquindio.UniEventos.modelo.TipoEvento;
import co.edu.uniquindio.UniEventos.modelo.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepo extends MongoRepository<Evento, String> {

    // Busca un evento por su nombre exacto
    Optional<Evento> findByNombre(String nombre);

    // Filtra eventos por nombre, tipo y ciudad, con coincidencias parciales (insensible a mayúsculas)
    @Query("{'nombre': {$regex: ?0, $options: 'i'}, 'tipo': {$regex: ?1, $options: 'i'}, 'ciudad': {$regex: ?2, $options: 'i'}}")
    List<Evento> filtrarEventos(String nombre, String tipo, String ciudad);

    // Busca eventos en un rango de fechas
    @Query("{'fecha': { $gte: ?0, $lte: ?1 }}")
    List<Evento> buscarPorRangoDeFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Busca eventos por tipo
    List<Evento> findByTipo(TipoEvento tipo);

    // Busca eventos por ciudad
    List<Evento> findByCiudad(String ciudad);

    // Busca eventos con alojamientos cercanos disponibles en una ciudad específica
    @Query("{ 'ciudad': ?0, 'alojamientosCercanos': { $exists: true, $not: { $size: 0 } }, 'alojamientosCercanos.direccion': { $regex: ?1, $options: 'i' } }")
    List<Evento> buscarConAlojamientosCercanos(String ciudad, String direccion);

}
