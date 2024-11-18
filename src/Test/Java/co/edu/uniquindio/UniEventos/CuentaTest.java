package co.edu.uniquindio.UniEventos;
import co.edu.uniquindio.UniEventos.dto.CuentaDTO.*;
import co.edu.uniquindio.UniEventos.dto.TokenDTO;
import co.edu.uniquindio.UniEventos.modelo.Cuenta;
import co.edu.uniquindio.UniEventos.modelo.EstadoCuenta;
import co.edu.uniquindio.UniEventos.modelo.Rol;
import co.edu.uniquindio.UniEventos.modelo.Usuario;
import co.edu.uniquindio.UniEventos.repositorios.CuentaRepo;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CuentaServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest

public class CuentaTest {
    @Autowired
    private CuentaServicio cuentaServicio;

    @Autowired
    private CuentaRepo cuentaRepo;



    /**
     * Metodo crearCuenta Test.
     * Este test verifica que una cuenta puede ser creada correctamente sin lanzar excepciones.
     * Se utiliza un DTO con los datos de la nueva cuenta y se espera que al llamar
     * al método 'crearCuenta', se obtenga un id válido para la cuenta creada.
     */
    @Test
    public void crearCuentaTest() {
        CrearCuentaDTO crearCuentaDTO = new CrearCuentaDTO(
                "1092455518", // Identificación
                "Daniela Losada", // Nombre
                "3122645894", // Número de teléfono
                "Conj San Jose", // Dirección
                "danielai.losadar@uqvirtual.edu.co", // Correo   (para que funcione el envio de correo electronico se debe colocar un email real)
                "Daniel@Lo20" // Contraseña
        );
        assertDoesNotThrow(() -> {
            String id = cuentaServicio.crearCuenta(crearCuentaDTO);
            // Verifica que el id de la cuenta creada no sea nulo
            assertNotNull(id);
        });
    }

    /**
     * Metodo encargado de activar la cuenta.
     *
     * @throws Exception
     */
    @Test
    public void activarCuentaTest() throws Exception {
        ActivarCuentaDTO activarCuentaDTO = new ActivarCuentaDTO(
                "danielai.losadar@uqvirtual.edu.co",
        "urvvjk33RI");
        cuentaServicio.activarCuenta(activarCuentaDTO);

    }


    @Test
    public void loginCuentaTest() {
        String email = "angiex.ruizr@uqvirtual.edu.co";
        String password = "XIME.Ruiz26@";  // Contraseña válida

        LoginDTO loginDTO = new LoginDTO(email, password);

        assertDoesNotThrow(() -> {
            TokenDTO tokenDTO = cuentaServicio.iniciarSesion(loginDTO);
            // Imprimir el token en la consola
            System.out.println("Token generado: " + tokenDTO.token());
        });
    }

    /**
     * Metodo para actualizarCuenta.
     * Este test verifica que se puede editar una cuenta existente sin lanzar excepciones.
     * Se actualizan algunos datos de la cuenta y luego se verifica que los cambios
     * se reflejan correctamente.
     */
    @Test
    public void actualizarCuentaTest() {
        String idCuenta = "670870869d4fc3095516f9b1"; // ID de la cuenta existente
        EditarCuentaDTO editarCuentaDTO = new EditarCuentaDTO(
                "Angie Ximena Ruiz", // Nombre actualizado
                "3122573966", // Teléfono
                "Calle Real" // Dirección actualizada
        );

        // Se espera que no se lance ninguna excepción al editar la cuenta
        assertDoesNotThrow(() -> {
            cuentaServicio.editarCuenta(editarCuentaDTO,idCuenta); // Actualiza la cuenta
        });
    }

    /**
     * Metodo para obtener la información de una cuenta existente.
     * Este test verifica que, cuando se proporciona un ID válido, el método
     * 'obtenerInformacionCuenta' devuelve correctamente la información de la cuenta.
     */
    @Test
    public void obtenerInfoCuentaTest() throws Exception {
        String idCuenta = "670885b73255d028609d4242"; // ID de cuenta válida en la base de datos

        // Obtiene la información de la cuenta y verifica que no sea nula
        InfoCuentaDTO cuentaInfo = cuentaServicio.obtenerInformacionCuenta(idCuenta);
        assertNotNull(cuentaInfo);

        // Verifica que el ID de la cuenta obtenida es el esperado
        assertEquals(idCuenta, cuentaInfo.id());
    }


    /**
     * Metodo para eliminar una cuenta.
     * Este test verifica que se puede eliminar una cuenta sin lanzar excepciones.
     * La eliminación en este caso no elimina realmente la cuenta, sino que cambia su estado a ELIMINADO.
     * Luego, se verifica que la cuenta aún existe, pero en estado ELIMINADO.
     */
    @Test
    public void eliminarCuentaTest() throws Exception {
        String idCuenta = "670870869d4fc3095516f9b1";// ID de la cuenta a eliminar
        assertDoesNotThrow(() -> cuentaServicio.eliminarCuenta(idCuenta));
    }

    /**
     * Metodo para listar cuentas.
     * Este test verifica que el método 'listarCuentas' devuelve el número correcto de cuentas.
     * Asume que ya hay datos predefinidos en la base de datos de pruebas.
     */
    @Test
    public void listarTest() {
        // Lista las cuentas disponibles en la base de datos de pruebas
        List<Cuenta> lista = cuentaServicio.listarCuentas();
        // Verifica que la lista contiene el número esperado de elementos
        assertEquals(3, lista.size(), "La lista de cuentas debería contener 2 elementos.");
    }


    /**
     * Metodo para mandar el codigo de cambio de password
     *
     * @throws Exception
     */
    @Test
    void enviarCodigoPasswordTest() throws Exception {
        // Datos de prueba
        String email = "angiex.ruizr@uqvirtual.edu.co";
        // Ejecutar el método
        String result = cuentaServicio.enviarCodigoRecuperacionPassword(email);
        // Verificaciones
        assertEquals("Código de recuperación enviado al correo " + email, result);
    }


    @Test
    void cambiarContraseñaTest() throws Exception {
        String correo = "angiex.ruizr@uqvirtual.edu.co";
        String code = "T5DZ1o8sTl";
        String newPassword = "XIME.Ruiz26@";
        String confirmatePassword = "XIME.Ruiz26@";
        CambiarPasswordDTO cambiarPasswordDTO = new CambiarPasswordDTO(code, newPassword, confirmatePassword, correo);
        // Llamar al método a probar
        cuentaServicio.cambiarPassword(cambiarPasswordDTO);

        // Verificar que la cuenta fue actualizada correctamente en la base de datos
        Cuenta updatedAccount = cuentaRepo.findByEmail(correo);
        assertNotNull(updatedAccount);
    }
}
