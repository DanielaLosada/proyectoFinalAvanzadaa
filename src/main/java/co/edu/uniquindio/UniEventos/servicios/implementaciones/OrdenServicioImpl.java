package co.edu.uniquindio.UniEventos.servicios.implementaciones;

import co.edu.uniquindio.UniEventos.dto.CuponDTO.CrearCuponDTO;
import co.edu.uniquindio.UniEventos.dto.EmailDTO.EmailDTO;
import co.edu.uniquindio.UniEventos.dto.OrdenDTO.CrearOrdenDTO;
import co.edu.uniquindio.UniEventos.dto.OrdenDTO.DetalleOrdenDTO;
import co.edu.uniquindio.UniEventos.dto.OrdenDTO.InfoOrdenDTO;
import co.edu.uniquindio.UniEventos.dto.OrdenDTO.ItemOrdenDTO;
import co.edu.uniquindio.UniEventos.excepciones.CuentaException;
import co.edu.uniquindio.UniEventos.excepciones.OrdenException;
import co.edu.uniquindio.UniEventos.modelo.*;
import co.edu.uniquindio.UniEventos.repositorios.CuentaRepo;
import co.edu.uniquindio.UniEventos.repositorios.EventoRepo;
import co.edu.uniquindio.UniEventos.repositorios.OrdenRepo;
import co.edu.uniquindio.UniEventos.servicios.interfaces.*;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrdenServicioImpl implements OrdenServicio {

    private final CuentaServicio cuentaServicio;
    private final EventoServicio eventoServicio;
    private final CuponServicio cuponServicio;
    private final EmailServicio emailServicio;
    private final QRServicioImpl qrServicio;
    private final ImagenesServicio imagenesServicio;
    private final CuentaRepo cuentaRepo;
    private final EventoRepo eventoRepo;
    private final OrdenRepo ordenRepo;

    @Override
    public String crearOrden(CrearOrdenDTO crearOrdenDTO) throws Exception {
        if (crearOrdenDTO == null) {
            throw new OrdenException("No se puede crear una orden");
        }
        double total = 0;
        for (DetalleOrden item : crearOrdenDTO.itemsOrden()) {
            total += item.getPrecio() * item.getCantidad();
        }
        Orden orden = Orden.builder()
                .id(ObjectId.get().toString())
                .idUser(crearOrdenDTO.idUsuario())
                .fecha(crearOrdenDTO.fecha())
                .codigoPasarela(crearOrdenDTO.codigoPasarela())
                .items(crearOrdenDTO.itemsOrden())
                .pago(crearOrdenDTO.pago().toEntity())
                .total(crearOrdenDTO.total())
                .codigoCupon(crearOrdenDTO.codigoCupon() != null ? crearOrdenDTO.codigoCupon() : null)
                .build();
        if (cuentaRepo.findById(crearOrdenDTO.idUsuario()).isEmpty()) {
            throw new Exception("El id de la cuenta no existe");
        }
        if (eventoRepo.findById(String.valueOf(crearOrdenDTO.itemsOrden().get(0).getIdEvento())).isEmpty()) {
            throw new Exception("El ID del evento no existe");
        }
        ObjectId userId = new ObjectId(crearOrdenDTO.idUsuario());
        List<Orden> ordenesCuenta = ordenRepo.buscarPorUser(userId);
        if (ordenesCuenta.isEmpty()) {
            System.out.println("¡Esta es la primera orden para la cuenta: " + crearOrdenDTO.idUsuario() + "!");
            Optional<Cuenta> cuentaOpt = cuentaRepo.findById(crearOrdenDTO.idUsuario());
            generarCupon1Orden(cuentaOpt.get().getEmail());
        }
        // Guardar la orden en la base de datos
        Orden ordenGuardada = ordenRepo.save(orden);
        // Aplicar cupón si está presente
        if (crearOrdenDTO.codigoCupon() != null) {
            // Aquí puedes invocar el servicio de aplicar el cupón
            cuponServicio.redimirCupon(crearOrdenDTO.codigoCupon(), ordenGuardada.getId());
        }
//        // Generar el QR de la orden en base64
//        String qrBase64 = qrServicio.generateQRCode(orden.getId());
//
//        // Convertir la base64 a byte array
//        byte[] qrBytes = Base64.getDecoder().decode(qrBase64);
//
//        // Subir el QR como imagen a Firebase
//        String qrUrl = imagenesServicio.subirQR(qrBytes, "order-" + orden.getId() + "-qr.png");
//
//        // Enviar el QR por email
//        Optional<Cuenta> account = cuentaRepo.findOptionalByEmail(String.valueOf(orden.getIdUser()));
//        if (account.isPresent()) {
//            String email = account.get().getEmail();
//            emailServicio.enviarQR(email, qrUrl);
//        } else {
//            // Manejo de caso en que no se encuentre la cuenta
//            throw new CuentaException("El email no existe para la cuenta con ID: " + orden.getIdUser());
//        }
        return "Orden guardada";
    }

    private void generarCupon1Orden(String email) throws Exception {
        CrearCuponDTO cuponDTO = new CrearCuponDTO(
                cuponServicio.generarCodigoCupon(),
                "Cupón de Bienvenida",              // Nombre del cupón,         // Código único de cupón
                15,
                TipoCupon.UNICO,
                EstadoCupon.DISPONIBLE, // Descuento del 15%
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now()
                // Fecha de expiración (30 días a partir de ahora// ID del evento: (en este caso no es requerido)
        );
        String couponId = cuponServicio.crearCupones(cuponDTO);
        // Enviar el correo con el código de cupón
        emailServicio.enviarCuponCompra(email, cuponDTO.codigo());
    }

    @Override
    public String cancelarOrden(String ordenId) throws Exception {
        Optional<Orden> orden = ordenRepo.findById(ordenId);
        if (orden.isEmpty()) {
            throw new Exception("No se puede eliminar la orden ya que no existe");
        }
        List<DetalleOrden> items = orden.get().getItems();
        for(DetalleOrden item : items){
            Evento evento= eventoServicio.obtenerEvento(item.getIdEvento());
            Localidad localidad = evento.obtenerLocalidad(item.getNombreLocalidad());
            int vendidas = localidad.getEntradasVendidas()-item.getCantidad();
            eventoServicio.actualizarCapacidadLocalidad(evento, item.getNombreLocalidad(), vendidas);
        }
        ordenRepo.deleteById(ordenId);
        return "La orden se cancelo (elimino) correctamente";
    }

    @Override
    public InfoOrdenDTO obtenerInfoOrden(String ordenId) throws OrdenException, Exception {
        Optional<Orden> orden = ordenRepo.findById(ordenId);
        if(orden.isEmpty()){
            throw new OrdenException("No se encontró la orden con el ID dado");
        }
        List<ItemOrdenDTO> detalleOrdenes = new ArrayList<>();
        for( DetalleOrden detail : orden.get().getItems()){
            Evento evento = eventoServicio.obtenerEvento(detail.getIdEvento());
            ItemOrdenDTO detalle = new ItemOrdenDTO(evento.getNombre(),detail.getNombreLocalidad(), detail.getCantidad(), detail.getPrecio() );
            detalleOrdenes.add(detalle);
        }
        return new InfoOrdenDTO(ordenId, orden.get().getIdUser(),orden.get().getFecha() ,detalleOrdenes, orden.get().getTotal() );
    }

    @Override
    public Orden obtenerOrden(String idOrden) throws Exception {
        Optional<Orden> ordenOpt = ordenRepo.findById(idOrden);
        if (ordenOpt.isPresent()) {
            return ordenOpt.get();
        } else {
            throw new Exception("Orden no encontrada con ID: " + idOrden);
        }
    }

    @Override
    public List<DetalleOrdenDTO> obtenerHistorialOrdenes(String idCuenta) throws Exception,OrdenException,CuentaException {
        Cuenta cuenta = cuentaServicio.obtenerCuenta(idCuenta);
        if(cuenta == null){
            throw new CuentaException("No existe una cuenta con ese ID");
        }
        List<Orden> ordenes = ordenRepo.findAllByIdUser(idCuenta);
        if(ordenes.isEmpty()){
            throw new OrdenException("No hay ordenes vinculadas a esta cuenta");
        }
        List<DetalleOrdenDTO> historialOrdenes = new ArrayList<>();
        for(Orden orden : ordenes){
            DetalleOrden detalleOrden = orden.getItems().get(0);
            Evento evento = eventoServicio.obtenerEvento(detalleOrden.getIdEvento());
            DetalleOrdenDTO detalleOrdenDTO = new DetalleOrdenDTO(evento.getImagenPortada(),evento.getNombre(), evento.getFecha(), detalleOrden.getNombreLocalidad(), detalleOrden.getCantidad(), orden.getTotal());
            historialOrdenes.add(detalleOrdenDTO);
        }
        return historialOrdenes;
    }
    @Override
    public List<Orden> listarOrdenesCliente(String userId) throws OrdenException, CuentaException, Exception {
        return ordenRepo.findAllByIdUser(userId);
    }
    @Override
    public List<Orden> getAllOrders() throws Exception {
        return ordenRepo.findAll();
    }
    @Override
    public Preference realizarPago(String ordenId) throws Exception {
        // Obtener la orden guardada en la base de datos y los ítems de la orden
        Orden ordenGuardada = obtenerOrden(ordenId);
        List<PreferenceItemRequest> itemsPasarela = new ArrayList<>();
        // Recorrer los items de la orden y crea los ítems de la pasarela
        for(DetalleOrden item : ordenGuardada.getItems()){
            // Obtener el evento y la localidad del ítem
            Evento evento = eventoServicio.obtenerEvento(item.getIdEvento());
            Localidad localidad = evento.obtenerLocalidad(item.getNombreLocalidad());
            // Crear el item de la pasarela
            PreferenceItemRequest itemRequest =
                    PreferenceItemRequest.builder()
                            .id(evento.getId())
                            .title(evento.getNombre())
                            .pictureUrl(evento.getImagenPortada())
                            .categoryId(evento.getTipo().name())
                            .quantity(item.getCantidad())
                            .currencyId("COP")
                            .unitPrice(BigDecimal.valueOf(localidad.getPrecio()))
                            .build();
            itemsPasarela.add(itemRequest);
            System.out.println("Precio unitario de la localidad: " + localidad.getPrecio());
        }
        // Configurar las credenciales de MercadoPago
        MercadoPagoConfig.setAccessToken("APP_USR-7341854364024225-101008-f37975cc5fa74052389028b963be8c3c-2026488557");
        // Configurar las urls de retorno de la pasarela (Frontend)
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success("URL PAGO EXITOSO")
                .failure("URL PAGO FALLIDO")
                .pending("URL PAGO PENDIENTE")
                .build();
        // Construir la preferencia de la pasarela con los ítems, metadatos y urls de retorno
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .backUrls(backUrls)
                .items(itemsPasarela)
                .metadata(Map.of("id_orden", ordenGuardada.getId()))
                .notificationUrl(" https://c2e6-152-202-14-177.ngrok-free.app/orden/notificacion-pago")
                .build();
        // Crear la preferencia en la pasarela de MercadoPago
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);
        // Guardar el código de la pasarela en la orden
        ordenGuardada.setCodigoPasarela( preference.getId() );
        ordenRepo.save(ordenGuardada);
        return preference;
    }

    @Override
    public void recibirNotificacionMercadoPago(Map<String, Object> request) {
        try {
            // Obtener el tipo de notificación
            Object tipo = request.get("type");
            // Si la notificación es de un pago entonces obtener el pago y la orden asociada
            if ("payment".equals(tipo)) {
                // Capturamos el JSON que viene en el request y lo convertimos a un String
                String input = request.get("data").toString();
                // Extraemos los números de la cadena, es decir, el id del pago
                String idPago = input.replaceAll("\\D+", "");
                // Se crea el cliente de MercadoPago y se obtiene el pago con el id
                PaymentClient client = new PaymentClient();
                Payment payment = client.get( Long.parseLong(idPago) );
                // Obtener el id de la orden asociada al pago que viene en los metadatos
                String idOrden = payment.getMetadata().get("id_orden").toString();
                // Se obtiene la orden guardada en la base de datos y se le asigna el pago
                Orden orden = obtenerOrden(idOrden);
                Pago pago = crearPago(payment);
                orden.setPago(pago);
                ordenRepo.save(orden);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Pago crearPago(Payment payment) {
        Pago pago = new Pago();
        pago.setId(payment.getId().toString());
        pago.setFecha( payment.getDateCreated().toLocalDateTime() );
        pago.setEstado(EstadoPago.valueOf(payment.getStatus()));
        pago.setDetalleEstado(payment.getStatusDetail());
        pago.setTipoPago(TipoPago.valueOf(payment.getPaymentTypeId()));
        pago.setMoneda(payment.getCurrencyId());
        pago.setCodigoAutorizacion(payment.getAuthorizationCode());
        pago.setValorTransaccion(payment.getTransactionAmount().floatValue());
        return pago;
    }

    @Override
    public String enviarCorreoInfoOrdenQR(String email, Orden order) throws Exception {
        Cuenta account = cuentaServicio.obtenerCuentaXEmail(email);
        String qrCodeUrl = "https://quickchart.io/qr?text=" + order.getId() + "&size=300";
        byte[] qrCodeImage = emailServicio.downloadImage(qrCodeUrl);
        String subject = "QR";
        StringBuilder body  = new StringBuilder();
        body.append("<html><body>");
        body.append("<h1>Hola ").append(account.getUser().getNombre()).append("!</h1>");
        body.append("<p>Gracias por tu compra. A continuación, encontrarás un resumen de tu orden:</p>");
        body.append("<h3>Resumen de la orden:</h3>");
        body.append("<p>Número de orden: ").append(order.getId()).append("<br>")
                .append("Fecha de compra: ").append(order.getFecha()).append("</p>");
        if (order.getPago() != null) {
            body.append("<p>Método de pago: ").append(order.getPago().getTipoPago().toString()).append("<br>")
                    .append("Estado del pago: ").append(order.getPago().getEstado()).append("</p>");
            body.append("<h3>Detalles del evento:</h3>");
            for (DetalleOrden item : order.getItems()) {
                Evento event = eventoServicio.obtenerEvento(item.getIdEvento().toString());
                body.append("<p>---------------------------------<br>")
                        .append("Evento: ").append(event.getNombre()).append("<br>")
                        .append("Fecha del evento: ").append(event.getFecha()).append("<br>")
                        .append("Localidad: ").append(item.getNombreLocalidad()).append("<br>")
                        .append("Número de boletos: ").append(item.getCantidad()).append("<br>")
                        .append("Total: ").append(item.getPrecio()).append("<br>")
                        .append("---------------------------------</p>");
            }
            body.append("<p>Total pagado: ").append(order.getTotal()).append("</p>");
            if (order.getCodigoCupon() != null) {
                Cupon coupon = cuponServicio.obtenerCupon(order.getCodigoCupon());
                body.append("<p>Cupón usado: ").append(coupon.getCodigo()).append("<br>")
                        .append("Descuento aplicado: ").append(coupon.getDescuento() * 100).append("%</p>");
            }
            body.append("<p>Para acceder a tus boletos, escanea el siguiente código QR:</p>");
            body.append("<img src='cid:qrCodeImage'/>");
            body.append("<br><p>¡Esperamos que disfrutes del evento!<br>Atentamente,<br>El equipo de UniEventos</p>");
            body.append("</body></html>");
        }
        return "Los detalles de su orden fueron enviados a su correo";
    }
}
