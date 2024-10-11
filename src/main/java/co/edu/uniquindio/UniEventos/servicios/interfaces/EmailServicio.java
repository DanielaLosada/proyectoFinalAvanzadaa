package co.edu.uniquindio.UniEventos.servicios.interfaces;

import co.edu.uniquindio.UniEventos.dto.EmailDTO.EmailDTO;

import java.io.IOException;

public interface EmailServicio {
    void enviarCorreo(EmailDTO emailDTO) throws Exception;

    void enviarQR(String email, String qrImageUrl) throws Exception;

    byte[] downloadImage(String imageUrl) throws IOException;

    void enviarCodigoActivacion(String email, String codigoActivacion) throws Exception;
    void enviarCodigoPassword(String email,  String codigoPassword) throws Exception;
    void enviarCuponBienvenida(String email, String codigoCupon) throws Exception;
    void enviarCuponCompra(String email, String codigoCupon) throws Exception;
}
