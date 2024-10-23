package co.edu.uniquindio.UniEventos.servicios.implementaciones.Utils;

import co.edu.uniquindio.UniEventos.dto.OrdenDTO.OrdenReporteDTO;
import co.edu.uniquindio.UniEventos.modelo.DetalleOrden;
import co.edu.uniquindio.UniEventos.modelo.EstadoCuenta;
import co.edu.uniquindio.UniEventos.modelo.EstadoEvento;
import co.edu.uniquindio.UniEventos.modelo.Orden;
import co.edu.uniquindio.UniEventos.repositorios.CuentaRepo;
import co.edu.uniquindio.UniEventos.repositorios.EventoRepo;
import co.edu.uniquindio.UniEventos.repositorios.OrdenRepo;
import lombok.RequiredArgsConstructor;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RequiredArgsConstructor
public class Reportes {
    private final OrdenRepo orderRepository;
    private final EventoRepo eventRepository;
    private final CuentaRepo accountRepository;

    public List<OrdenReporteDTO> generarReporteVentasPorLocalidad() {
        // Obtener todas las órdenes
        List<Orden> ordenes = orderRepository.findAll();

        // Agrupar ventas por localidad y sumar las ganancias
        Map<String, Double> ventasPorLocalidad = new HashMap<>();
        for (Orden orden : ordenes) {
            for (DetalleOrden detalle : orden.getItems()) {
                String localidad = String.valueOf(detalle.getNombreLocalidad());
                double totalVentas = detalle.getPrecio() * detalle.getCantidad();
                ventasPorLocalidad.put(localidad, ventasPorLocalidad.getOrDefault(localidad, 0.0) + totalVentas);
            }
        }

        // Convertir la información en un DTO para retornar
        List<OrdenReporteDTO> datosReporte = new ArrayList<>();
        ventasPorLocalidad.forEach((localidad, total) -> {
            datosReporte.add(new OrdenReporteDTO(localidad, total));
        });

        return datosReporte;
    }

    /** Método para generar el PDF
     *
     * @return
     */
    public byte[] generarReporteVentasPDF() throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            // Crear el escritor PDF
            PdfWriter escritor = new PdfWriter(byteArrayOutputStream);
            PdfDocument pdf = new PdfDocument(escritor);
            Document documento = new Document(pdf);

            // Agregar título grande centrado
            Paragraph titulo = new Paragraph("Reporte de Ventas").setBold().setFontSize(18).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.CENTER);
            documento.add(titulo);

            // Agregar fecha en la parte superior derecha
            String fechaFormateada = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            Paragraph fecha = new Paragraph("Fecha: " + fechaFormateada).setTextAlignment(com.itextpdf.layout.properties.TextAlignment.RIGHT);
            documento.add(fecha);

            // Tabla de ventas por localidad
            List<OrdenReporteDTO> reporteVentas = generarReporteVentasPorLocalidad();
            documento.add(new Paragraph("Ventas por Localidad"));
            Table tablaVentas = new Table(2);
            tablaVentas.addHeaderCell("Localidad");
            tablaVentas.addHeaderCell("Total Vendido");
            for (OrdenReporteDTO reporte : reporteVentas) {
                tablaVentas.addCell(reporte.localidad());
                tablaVentas.addCell(String.valueOf(reporte.total()));
            }
            documento.add(tablaVentas);

            // Tabla de eventos más vendidos
            Map<String, Integer> eventosMasVendidos = generarEventosMasVendidos();
            documento.add(new Paragraph("Eventos más Vendidos"));
            Table tablaEventos = new Table(2);
            tablaEventos.addHeaderCell("Evento");
            tablaEventos.addHeaderCell("Cantidad Vendida");
        /* eventosMasVendidos.forEach((evento, ventas) -> {
            tablaEventos.addCell(evento);
            tablaEventos.addCell(String.valueOf(ventas));
        }); */
            documento.add(tablaEventos);

            // Tabla de usuarios activos
            long usuariosActivos = contarUsuariosActivos();
            documento.add(new Paragraph("Usuarios Activos: " + usuariosActivos));

            // Tabla de eventos activos
            long eventosActivos = contarEventosActivos();
            documento.add(new Paragraph("Eventos Activos: " + eventosActivos));

            // Cerrar el documento
            documento.close();

            // Guardar el PDF en el escritorio
            String homeDir = System.getProperty("user.home");
            String os = System.getProperty("os.name").toLowerCase();
            Path rutaEscritorio;

            if (os.contains("win") || os.contains("mac")) {
                rutaEscritorio = Paths.get(homeDir, "Desktop", "Reporte_Ventas.pdf");
            } else {
                rutaEscritorio = Paths.get(homeDir, "Escritorio", "Reporte_Ventas.pdf");
            }

            Files.write(rutaEscritorio, byteArrayOutputStream.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return byteArrayOutputStream.toByteArray();
    }

    /** Métodos para obtener datos adicionales
     *
     * @return
     */
    private Map<String, Integer> generarEventosMasVendidos() {
        List<Orden> ordenes = orderRepository.findAll();
        Map<String, Integer> ventasEventos = new HashMap<>();

        for (Orden orden : ordenes) {
            for (DetalleOrden detalle : orden.getItems()) {
                String nombreEvento = detalle.getIdEvento();
                ventasEventos.put(nombreEvento, ventasEventos.getOrDefault(nombreEvento, 0) + detalle.getCantidad());
            }
        }
        return ventasEventos;
    }

    /** Método que cuenta los usuarios activos en la aplicación.
     *
     * @return
     */
    private long contarUsuariosActivos() {
        return accountRepository.countByEstado(EstadoCuenta.ACTIVA);  // Cuenta los usuarios activos
    }

    /** Método que cuenta los eventos activos en la aplicación.
     *
     * @return
     */
    private long contarEventosActivos() {
        return eventRepository.countByEstado(EstadoEvento.ACTIVO);  // Cuenta los eventos activos
    }
}
