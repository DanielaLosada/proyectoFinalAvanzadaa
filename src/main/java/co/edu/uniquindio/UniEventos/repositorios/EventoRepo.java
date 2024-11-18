package co.edu.uniquindio.UniEventos.repositorios;

import co.edu.uniquindio.UniEventos.modelo.*;
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
    @Query("{ $and: ["
            + "{ $or: [ { 'nombre': { $regex: ?0, $options: 'i' } }, { ?0: { $exists: false } } ] },"
            + "{ $or: [ { 'tipo': { $regex: ?1, $options: 'i' } }, { ?1: { $exists: false } } ] },"
            + "{ $or: [ { 'ciudad': { $regex: ?2, $options: 'i' } }, { ?2: { $exists: false } } ] }"
            + "] }")
    List<Evento> filtrarEventos(String nombre, String tipo, String ciudad);


    long countByEstado(EstadoEvento estado);

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

    // Búsqueda solo por nombre
    List<Evento> findByNombreRegex(String nombre);

    // Búsqueda solo por ciudad
    List<Evento> findByCiudadRegex(String ciudad);

    // Búsqueda por nombre y tipo
    List<Evento> findByNombreRegexAndTipo(String nombre, String tipo);

    // Búsqueda por nombre y ciudad
    List<Evento> findByNombreRegexAndCiudadRegex(String nombre, String ciudad);

    // Búsqueda por tipo y ciudad
    List<Evento> findByTipoAndCiudadRegex(String tipo, String ciudad);

    // Búsqueda por nombre, tipo y ciudad
    List<Evento> findByNombreRegexAndTipoAndCiudadRegex(String nombre, String tipo, String ciudad);

}
