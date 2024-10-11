package co.edu.uniquindio.UniEventos.dto.OrdenDTO;

import co.edu.uniquindio.UniEventos.modelo.DetalleOrden;
import co.edu.uniquindio.UniEventos.modelo.Pago;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

public record InfoOrdenDTO(
        String id,
        String idUser,
        LocalDateTime fecha,
        List<ItemOrdenDTO> items,
        float total
       ) {
}
