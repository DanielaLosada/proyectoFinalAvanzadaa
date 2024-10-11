package co.edu.uniquindio.UniEventos.servicios.implementaciones.Utils;

import co.edu.uniquindio.UniEventos.modelo.*;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CuentaServicio;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CuponServicio;
import co.edu.uniquindio.UniEventos.servicios.interfaces.EmailServicio;
import co.edu.uniquindio.UniEventos.servicios.interfaces.EventoServicio;

public class EmailPlantillas {

    public static String obtenerMensajeCodigoValidacion(String codigoValidacion) {
        return "Estimado usuario,\n\n" +
                "Gracias por registrarse. Su código de activación es: " + codigoValidacion + "\n\n" +
                "Este código es válido por 15 minutos.";
    }

    public static String obtenerMensajeRecuperacionPass(String codigoRecuperacionPass) {
        return "Estimado usuario,\n\n" +
                "Ha solicitado recuperar su contraseña. Utilice el siguiente código de recuperación para restablecer su contraseña:\n\n" +
                "Código de recuperación: " + codigoRecuperacionPass + "\n\n" +
                "Este código es válido por 15 minutos.\n\n" +
                "Si usted no solicitó esta recuperación, por favor ignore este correo.\n\n" +
                "Atentamente,\n" +
                "El equipo de UniEventos";
    }

    public static String obtenerMensajeCuponBienvenida(String codigoCupon) {
        return "Estimado usuario,\n\n" +
                "Gracias por registrarse en nuestra plataforma. Para celebrar su registro, le ofrecemos un cupón de bienvenida con un 15% de descuento en su próxima compra:\n\n" +
                "Código de cupón: " + codigoCupon + "\n\n" +
                "Este cupón es válido por 30 días y solo puede ser utilizado una vez.\n\n" +
                "Si tiene alguna duda, por favor contáctenos.\n\n" +
                "Atentamente,\n" +
                "El equipo de UniEventos";
    }

    public static String obtenerMensajeCuponCompra(String codigoCupon) {
        return "Estimado usuario,\n\n" +
                "Gracias por su compra. Para celebrar su primera compra, le ofrecemos un cupón con un 10% de descuento en su próxima compra:\n\n" +
                "Código de cupón: " + codigoCupon + "\n\n" +
                "Este cupón es válido por 30 días y solo puede ser utilizado una vez.\n\n" +
                "Si tiene alguna duda, por favor contáctenos.\n\n" +
                "Atentamente,\n" +
                "El equipo de UniEventos";
    }


}