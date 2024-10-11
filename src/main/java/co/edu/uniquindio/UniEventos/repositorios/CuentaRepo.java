package co.edu.uniquindio.UniEventos.repositorios;
import co.edu.uniquindio.UniEventos.modelo.Cuenta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CuentaRepo extends MongoRepository<Cuenta, String> {
    // Busca una cuenta por el email y devuelve un objeto Cuenta
    Cuenta findByEmail(String email);

    // Busca una cuenta opcional por email
    Optional<Cuenta> findOptionalByEmail(String email);

    // Verifica si una cuenta con un ID específico existe
    boolean existsById(String id);

    // Busca una cuenta opcional por cédula
    @Query("{ 'user.cedula': ?0 }")
    Optional<Cuenta> findByCedula(String cedula);
}
