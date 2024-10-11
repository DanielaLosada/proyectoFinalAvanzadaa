package co.edu.uniquindio.UniEventos.repositorios;

import co.edu.uniquindio.UniEventos.modelo.Alojamiento;
import co.edu.uniquindio.UniEventos.modelo.TipoAlojamiento;
import co.edu.uniquindio.UniEventos.modelo.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlojamientoRepo extends MongoRepository<Alojamiento, String> {
    @Query("{nombre: ?0}")
    Optional<Alojamiento> buscarPorNombre(String nombre);

    @Query("{ciudad: ?0}")
    List<Alojamiento> buscarPorCiudad(String ciudad);

    @Query("{precioNoche: { $gte: ?0, $lte: ?1 }}")
    List<Alojamiento> buscarPorRangoDePrecios(float precioMin, float precioMax);

    @Query("{distanciaEvento: { $lte: ?0 }}")
    List<Alojamiento> buscarAlojamientosCercanos(float distanciaMax);

    @Query("{ciudad: ?0, precioNoche: { $gte: ?1, $lte: ?2 }}")
    List<Alojamiento> buscarPorCiudadYRangoDePrecios(String ciudad, float precioMin, float precioMax);

    @Query("{tipo: ?0, distanciaEvento: { $lte: ?1 }}")
    List<Alojamiento> buscarPorTipoYDistancia(TipoAlojamiento tipo, float distanciaMax);

}
