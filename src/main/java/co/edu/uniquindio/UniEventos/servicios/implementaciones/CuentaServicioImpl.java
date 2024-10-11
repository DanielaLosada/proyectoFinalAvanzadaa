package co.edu.uniquindio.UniEventos.servicios.implementaciones;

import co.edu.uniquindio.UniEventos.config.JWTUtils;
import co.edu.uniquindio.UniEventos.dto.CuentaDTO.*;
import co.edu.uniquindio.UniEventos.dto.CuponDTO.CrearCuponDTO;
import co.edu.uniquindio.UniEventos.dto.TokenDTO;
import co.edu.uniquindio.UniEventos.excepciones.CuentaException;
import co.edu.uniquindio.UniEventos.modelo.*;
import co.edu.uniquindio.UniEventos.repositorios.CuentaRepo;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CuentaServicio;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CuponServicio;
import co.edu.uniquindio.UniEventos.servicios.interfaces.EmailServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CuentaServicioImpl implements CuentaServicio {
    private final CuentaRepo cuentaRepo;
    private final EmailServicio emailService;
    private final CuponServicio cuponServicio;
    private final JWTUtils jwtUtils;

    @Override
    public String crearCuenta(CrearCuentaDTO cuenta) throws Exception {
        if (existeEmail(cuenta.email())) {
            throw new CuentaException("El correo:" + cuenta.email() + " ya existe");
        }
        if (existeCedula(cuenta.cedula())) {
            throw new CuentaException("La cedula:" + cuenta.cedula() + " ya existe");
        }
        String passwordEncrip = encriptarPassword(cuenta.password());
        Cuenta nuevaCuenta = new Cuenta();
        nuevaCuenta.setRol(Rol.CLIENTE);
        nuevaCuenta.setEstado(EstadoCuenta.INACTIVA);
        nuevaCuenta.setEmail(cuenta.email());
        nuevaCuenta.setPassword(passwordEncrip);
        nuevaCuenta.setFechaRegistro(LocalDateTime.now());
        nuevaCuenta.setUser(new Usuario(
                cuenta.nombre(),
                cuenta.cedula(),
                cuenta.direccion(),
                cuenta.telefono()
        ));
        String codigoValidacion = generarCodigo();
        CodigoValidacion codigoValidacionObj = new CodigoValidacion(codigoValidacion, LocalDateTime.now());
        nuevaCuenta.setCodigoValidacionRegistro(codigoValidacionObj);
        Cuenta cuentaCreada = cuentaRepo.save(nuevaCuenta);
        emailService.enviarCodigoActivacion(cuentaCreada.getEmail(),codigoValidacion);
        return cuentaCreada.getId();
    }

    private String encriptarPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode( password );
    }

    private boolean existeEmail(String email) {
        return cuentaRepo.findOptionalByEmail(email).isPresent();
    }
    private boolean existeCedula(String cedula) {
        return cuentaRepo.findByCedula(cedula).isPresent();
    }

    @Override
    public boolean activarCuenta(ActivarCuentaDTO activarCuentaDTO) throws Exception {
        // Buscar la cuenta por correo
        Cuenta cuenta = cuentaRepo.findByEmail(activarCuentaDTO.email());
        if (cuenta == null) {
            throw new CuentaException("La cuenta no existe");
        }
        // Verificar si la cuenta ya está activada
        if (cuenta.getEstado().equals(EstadoCuenta.ACTIVA)) {
            throw new CuentaException("La cuenta ya está activada.");
        }
        // Obtener el código de validación de registro
        CodigoValidacion codigoValidacion = cuenta.getCodigoValidacionRegistro();
        if (codigoValidacion == null) {
            throw new CuentaException("El código de validación no existe.");
        }
        // Verificar si el código ha expirado
//        if (codigoValidacion.isExpired()) {
//            throw new CuentaException("El código de validación ha expirado.");
//        }
        // Verificar si el código es válido
        if (!codigoValidacion.getCodigo().equals(activarCuentaDTO.codigoValidacionRegistro())) {
            throw new CuentaException("El código de validación es inválido.");
        }
        // Crear el cupón usando el servicio de cupones
        CrearCuponDTO cuponDTO =  generarCuponBienvenida();
        cuponServicio.crearCupones(cuponDTO);

        // Enviar por email el cupon de bienvenida
        emailService.enviarCuponBienvenida(cuenta.getEmail(), cuponDTO.codigo());

        // Activar la cuenta y eliminar el código de validación
        cuenta.setCodigoValidacionRegistro(null);
        cuenta.setEstado(EstadoCuenta.ACTIVA);
        cuentaRepo.save(cuenta);
        return true;
    }

    private CrearCuponDTO generarCuponBienvenida() {
        // Creamos el cupon de 15% de descuento
        return new CrearCuponDTO(
                cuponServicio.generarCodigoCupon(),
                "Cupón de Bienvenida",
                15,
                TipoCupon.UNICO,
                EstadoCupon.DISPONIBLE,
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now()
        );
    }

    @Override
    public String editarCuenta(EditarCuentaDTO cuenta, String id) throws CuentaException {
        Optional<Cuenta> cuentaa = cuentaRepo.findById(id);
        if (cuentaa.isEmpty()) {
            throw new CuentaException("La cuenta no existe");
        }
        Cuenta cuentaActualizada = cuentaa.get();
        cuentaActualizada.getUser().setNombre(cuenta.nombre());
        cuentaActualizada.getUser().setTelefono(cuenta.telefono());
        cuentaActualizada.getUser().setDireccion(cuenta.direccion());

        cuentaRepo.save(cuentaActualizada);
        return cuentaActualizada.getId();
    }

    @Override
    public String eliminarCuenta(String id) throws Exception {
        Optional<Cuenta> optionalCuenta = cuentaRepo.findById(id);
        if(optionalCuenta.isEmpty()){
            throw new CuentaException("No se encontró el usuario con el id "+id);
        }
        //Obtenemos la cuenta del usuario que se quiere eliminar y le asignamos el estado eliminado
        Cuenta cuenta = optionalCuenta.get();
        cuenta.setEstado(EstadoCuenta.ELIMINADA);
        cuentaRepo.save(cuenta);
        return cuenta.getId();
    }

    @Override
    public InfoCuentaDTO obtenerInformacionCuenta(String id) throws CuentaException {
        Optional<Cuenta> optionalCuenta = cuentaRepo.findById(id);
        if (optionalCuenta.isEmpty()) {
            throw new CuentaException("No se encontró el usuario con el id " + id);
        }
        Cuenta cuenta = optionalCuenta.get();
        return new InfoCuentaDTO(
                cuenta.getId(),
                cuenta.getUser().getCedula(),
                cuenta.getUser().getNombre(),
                cuenta.getUser().getTelefono(),
                cuenta.getUser().getDireccion(),
                cuenta.getEmail()
        );
    }

    @Override
    public Cuenta obtenerCuenta(String id) throws Exception {
        Optional<Cuenta> cuentaOptional = cuentaRepo.findById(id);
        if (cuentaOptional.isEmpty()) {
            throw new Exception("No existe la cuenta");
        }
        return cuentaOptional.get();
    }

    @Override
    public Cuenta obtenerCuentaXEmail(String email) throws Exception {
        Optional<Cuenta> cuentaOpt = cuentaRepo.findOptionalByEmail(email);
        if (cuentaOpt.isEmpty()) {
            throw new Exception("Este email no esta registrado");
        }
        return cuentaOpt.get();
    }

    @Override
    public String enviarCodigoRecuperacionPassword(String correo) throws Exception {
        Optional<Cuenta> cuentaOpc = cuentaRepo.findOptionalByEmail(correo);
        if (cuentaOpc.isEmpty()) {
            throw new CuentaException("No se encontro el correo "+correo);
        }
        Cuenta cuenta = cuentaOpc.get();
        String codigoPassword = generarCodigo();
        CodigoValidacion codigoValidacion = new CodigoValidacion(codigoPassword, LocalDateTime.now());
        cuenta.setCodigoValidacionPassword(codigoValidacion);
        cuentaRepo.save(cuenta);
        emailService.enviarCodigoPassword(cuenta.getEmail(),codigoPassword);
        return "Código de recuperación enviado al correo " + cuenta.getEmail();
    }

    @Override
    public String cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) throws Exception {
        Optional<Cuenta> cuentaOpt = cuentaRepo.findOptionalByEmail(cambiarPasswordDTO.email());
        if (cuentaOpt.isEmpty()) {
            throw new CuentaException("No se encontro el correo");
        }
        Cuenta cuenta = cuentaOpt.get();
        CodigoValidacion codigoPassword = cuenta.getCodigoValidacionPassword();
        if (codigoPassword == null || codigoPassword.isExpired()) {
            throw new CuentaException("El codigo es invalido");
        }
        if (!codigoPassword.getCodigo().equals(cambiarPasswordDTO.codigoVerificacion())) {
            throw new CuentaException("El codigo es incorrecto");
        }
        // Verificar que las contraseñas ingresadas coinciden
        if (!cambiarPasswordDTO.passwordNueva().equals(cambiarPasswordDTO.confirmacionPassword())) {
            throw new CuentaException("Las contraseñas no coinciden.");
        }
        // Encriptar la nueva contraseña
        String passwordEncrip = encriptarPassword(cambiarPasswordDTO.passwordNueva());
        cuenta.setPassword(passwordEncrip);
        // Limpiar el código de recuperación para evitar su reutilización
        cuenta.setCodigoValidacionPassword(null);
        // Guardar la cuenta actualizada
        cuentaRepo.save(cuenta);
        return "La contraseña ha sido cambiada exitosamente.";
    }


    @Override
    public TokenDTO iniciarSesion(LoginDTO loginDTO) throws Exception {
        Optional<Cuenta> cuentaOpt = cuentaRepo.findOptionalByEmail(loginDTO.email());
        // Corregido: lanzar excepción si no se encuentra el email
        if (cuentaOpt.isEmpty()) {
            throw new CuentaException("No se encontro el correo");
        }
        Cuenta cuenta = cuentaOpt.get();
        if (!cuenta.getEstado().equals(EstadoCuenta.ACTIVA)) {
            throw new CuentaException("La cuenta no se encutra Activada. Porfavor activala para ingresar!");
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // Verificar si la contraseña es válida
        if (!passwordEncoder.matches(loginDTO.password(), cuenta.getPassword())) {
            throw new CuentaException("La contraseña es incorrecta");
        }
        Map<String, Object> map = construirClaims(cuenta);
        return new TokenDTO(jwtUtils.generateToken(cuenta.getEmail(), map));
    }

    private Map<String, Object> construirClaims(Cuenta cuenta) {
        return Map.of(
                "rol", cuenta.getRol(),
                "nombre", cuenta.getUser().getNombre(),
                "id", cuenta.getId()
        );
    }

    @Override
    public List<Cuenta> listarCuentas() {
        return cuentaRepo.findAll();
    }


    public String generarCodigo() {
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
}
