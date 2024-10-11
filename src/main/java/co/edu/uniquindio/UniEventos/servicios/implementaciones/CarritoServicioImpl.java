package co.edu.uniquindio.UniEventos.servicios.implementaciones;

import co.edu.uniquindio.UniEventos.dto.CarritoDTO.ActualizarCarritoDTO;
import co.edu.uniquindio.UniEventos.dto.CarritoDTO.CrearCarritoDTO;
import co.edu.uniquindio.UniEventos.dto.CarritoDTO.ItemCarritoDTO;
import co.edu.uniquindio.UniEventos.excepciones.CarritoException;
import co.edu.uniquindio.UniEventos.excepciones.EventoException;
import co.edu.uniquindio.UniEventos.modelo.*;
import co.edu.uniquindio.UniEventos.repositorios.CarritoRepo;
import co.edu.uniquindio.UniEventos.repositorios.CuentaRepo;
import co.edu.uniquindio.UniEventos.repositorios.EventoRepo;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CarritoServicio;
import co.edu.uniquindio.UniEventos.servicios.interfaces.CuentaServicio;
import co.edu.uniquindio.UniEventos.servicios.interfaces.EventoServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CarritoServicioImpl implements CarritoServicio {

    private final CarritoRepo carritoRepo;
    private final EventoRepo eventoRepo;
    private final CuentaRepo cuentaRepo;
    @Override
    public void añadirItemAlCarrito(String cuentaId, ItemCarritoDTO itemCarritoDTO) throws CarritoException {
        // Buscar el carrito por cuentaId
        Carrito carrito = carritoRepo.buscarCarritoPorIdCliente(cuentaId);
        if (carrito == null) {
            carrito = crearCarritoSiNoExiste(cuentaId); // Método extraído para crear el carrito
        }
        // Buscar el evento por ID
        Evento evento = eventoRepo.findById(itemCarritoDTO.idEvento())
                .orElseThrow(() -> new EventoException("No se encontró el evento con el ID: " + itemCarritoDTO.idEvento()));
        // Buscar la localidad dentro del evento
        Localidad localidad = buscarLocalidad(evento, itemCarritoDTO.nombreLocalidad());
        // Verificar si el ítem ya existe en el carrito y calcular la cantidad total
        int cantidadTotal = calcularCantidadTotal(carrito, itemCarritoDTO, evento);
        // Verificar aforo
        if (cantidadTotal > localidad.getAforo()-localidad.getEntradasVendidas()) {
            throw new IllegalArgumentException("No se puede agregar el ítem al carrito porque el aforo de la localidad está completo.");
        }
        // Crear el nuevo ítem del carrito
        DetalleCarrito newItem = crearNuevoItem(itemCarritoDTO, evento, localidad);
        // Agregar el ítem al carrito
        carrito.getItems().add(newItem);
        // Recalcular el total del carrito y guardar
        carrito.calcularTotal();
        carritoRepo.save(carrito);
    }

    // Método auxiliar para crear el carrito si no existe
    private Carrito crearCarritoSiNoExiste(String cuentaId) throws CarritoException {
        try {
            return crearCarrito(cuentaId);
        } catch (Exception e) {
            throw new CarritoException("Error al crear el carrito: " + e.getMessage());
        }
    }

    // Método auxiliar para buscar la localidad
    private Localidad buscarLocalidad(Evento evento, String nombreLocalidad) {
        return evento.getLocalidades().stream()
                .filter(loc -> loc.getNombre().equals(nombreLocalidad))
                .findFirst()
                .orElseThrow(() -> new EventoException("No se encontró la localidad: " + nombreLocalidad));
    }

    // Método auxiliar para calcular la cantidad total
    private int calcularCantidadTotal(Carrito carrito, ItemCarritoDTO itemCarritoDTO, Evento evento) {
        DetalleCarrito existingItem = carrito.getItems().stream()
                .filter(item -> item.getIdEvento().equals(evento.getId()) && item.getNombreLocalidad().equals(itemCarritoDTO.nombreLocalidad()))
                .findFirst()
                .orElse(null);

        // Si el ítem existe, sumamos la cantidad
        return existingItem != null ? existingItem.getCantidad() + itemCarritoDTO.cantidad() : itemCarritoDTO.cantidad();
    }

    // Método auxiliar para crear el nuevo ítem
    private DetalleCarrito crearNuevoItem(ItemCarritoDTO itemCarritoDTO, Evento evento, Localidad localidad) {
        return DetalleCarrito.builder()
                .idEvento(itemCarritoDTO.idEvento())
                .nombreEvento(evento.getNombre())
                .nombreLocalidad(itemCarritoDTO.nombreLocalidad())
                .precio(localidad.getPrecio())
                .cantidad(itemCarritoDTO.cantidad())
                .subtotal(localidad.getPrecio() * itemCarritoDTO.cantidad())
                .build();
    }

    @Override
    public void removerItemCarrito(String cuentaId, String eventoId) throws CarritoException {
        Carrito carrito = carritoRepo.buscarCarritoPorIdCliente(cuentaId);
        if (carrito == null) {
            throw new CarritoException("No se encontró el carrito para la cuenta: " + cuentaId);
        }
        // Verificar y eliminar el ítem del carrito
        boolean itemRemovido = carrito.getItems().removeIf(item -> item.getIdEvento().equals(eventoId));
        if (!itemRemovido) {
            throw new EventoException("No se encontró el evento con ID: " + eventoId + " en el carrito.");
        }
        // Guardar los cambios en el carrito
        carrito.calcularTotal();
        carritoRepo.save(carrito);
    }

    @Override
    public Carrito crearCarrito(String idUser) throws Exception {
        if (cuentaRepo.findById(idUser).isEmpty()) {
            throw new CarritoException("La cuenta con el ID especificado no existe.");
        }
        Carrito carrito = new Carrito();
        carrito.setIdUser(idUser);
        carrito.setItems(new ArrayList<DetalleCarrito>());
        carrito.setFecha(LocalDateTime.now());
        return carritoRepo.save(carrito);
    }

    @Override
    public void actualizarCarrito(String cuentaId, String eventoId, String localidadNombre, ActualizarCarritoDTO actualizarCarritoDTO) throws CarritoException {
        Carrito carrito = carritoRepo.buscarCarritoPorIdCliente(cuentaId);
        if (carrito == null) {
            throw new CarritoException("No se encontró el carrito para la cuenta: " + cuentaId);
        }
        // Buscar el ítem en el carrito
        DetalleCarrito item = carrito.getItems().stream()
                .filter(detail -> detail.getIdEvento().equals(eventoId) && detail.getNombreLocalidad().equals(localidadNombre))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Ítem no encontrado en el carrito"));

        // Si se cambia la localidad, buscar el nuevo precio y verificar disponibilidad
        if (!localidadNombre.equals(actualizarCarritoDTO.nombreLocalidad())) {
            Evento evento = eventoRepo.findById(eventoId)
                    .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

            Localidad nuevaLocalidad = evento.getLocalidades().stream()
                    .filter(loc -> loc.getNombre().equals(actualizarCarritoDTO.nombreLocalidad()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Nueva localidad no encontrada"));

            item.setNombreLocalidad(nuevaLocalidad.getNombre());
            item.setPrecio(nuevaLocalidad.getPrecio()); // Actualiza el precio según la nueva localidad
        }

        // Actualizar la cantidad y calcular el nuevo subtotal
        item.setCantidad(actualizarCarritoDTO.cantidad()); // Usar la cantidad del DTO
        item.setSubtotal(item.getPrecio() * item.getCantidad());

        // Recalcular el total del carrito
        carrito.calcularTotal();

        // Guardar el carrito actualizado en la base de datos
        carritoRepo.save(carrito);
    }

    @Override
    public void limpiarCarrito(String cuentaId) throws CarritoException {
        Carrito carrito = carritoRepo.buscarCarritoPorIdCliente(cuentaId);
        if (carrito == null) {
            throw new CarritoException("El carrito no existe para la cuenta: " + cuentaId);
        }
        carrito.setItems(new ArrayList<>());
        carrito.calcularTotal();
        carritoRepo.save(carrito);
    }

    @Override
    public List<DetalleCarrito> obtenerDetalleCarrito(String cuentaId) throws CarritoException {
        Carrito carrito = carritoRepo.buscarCarritoPorIdCliente(cuentaId);
        if (carrito == null) {
            throw new CarritoException("El carrito no existe para la cuenta: " + cuentaId);
        }
        // Obtener la lista de ítems del carrito
        List<DetalleCarrito> carritoDetalles = carrito.getItems();

        // Imprimir los detalles de cada ítem usando toString()
        carritoDetalles.forEach(item -> System.out.println(item.toString()));
        return carritoDetalles;
    }

}
