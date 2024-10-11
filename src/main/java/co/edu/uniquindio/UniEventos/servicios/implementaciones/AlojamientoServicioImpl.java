package co.edu.uniquindio.UniEventos.servicios.implementaciones;

import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.CrearAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.EditarAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.AlojamientoDTO.InfoAlojamientoDTO;
import co.edu.uniquindio.UniEventos.dto.EventoDTO.InfoEventoDTO;
import co.edu.uniquindio.UniEventos.dto.EventoDTO.ItemEventoDTO;
import co.edu.uniquindio.UniEventos.excepciones.EventoException;
import co.edu.uniquindio.UniEventos.modelo.*;
import co.edu.uniquindio.UniEventos.repositorios.AlojamientoRepo;
import co.edu.uniquindio.UniEventos.servicios.interfaces.AlojamientoServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AlojamientoServicioImpl implements AlojamientoServicio {

    private final AlojamientoRepo alojamientoRepo;

    @Override
    public String crearAlojamiento(CrearAlojamientoDTO crearAlojamientoDTO) throws Exception {
        if (existeNombre(crearAlojamientoDTO.nombre())) {
            throw new EventoException("El nombre ya existe, elija otro nombre");
        }
        Alojamiento alojamiento = new Alojamiento();
        alojamiento.setNombre(crearAlojamientoDTO.nombre());
        alojamiento.setCiudad(crearAlojamientoDTO.ciudad());
        alojamiento.setDistanciaEvento(crearAlojamientoDTO.distanciaEvento());
        alojamiento.setCiudad(crearAlojamientoDTO.ciudad());
        alojamiento.setTipo(crearAlojamientoDTO.tipo());
        alojamiento.setEstado(crearAlojamientoDTO.estado());
        alojamiento.setCalificacion(crearAlojamientoDTO.calificacion());
        alojamiento.setPrecioNoche(crearAlojamientoDTO.precioNoche());
        Alojamiento alojamientoCreado = alojamientoRepo.save(alojamiento);
        return alojamientoCreado.getId();
    }

    public boolean existeNombre(String nombre){
        return alojamientoRepo.buscarPorNombre(nombre).isPresent();
    }

    @Override
    public String editarAlojamiento(EditarAlojamientoDTO editarAlojamientoDTO, String id) throws Exception {
        Optional<Alojamiento> optionalAloj = alojamientoRepo.findById(id);
        if (optionalAloj.isEmpty()) {
            throw new Exception("No existe este alojamiento");
        }
        Alojamiento alojamientoModificado = optionalAloj.get();
        alojamientoModificado.setNombre(editarAlojamientoDTO.nombre());
        alojamientoModificado.setPrecioNoche(editarAlojamientoDTO.precioNoche());
        alojamientoModificado.setCalificacion(editarAlojamientoDTO.calificacion());
        alojamientoModificado.setTipo(editarAlojamientoDTO.tipo());
        alojamientoModificado.setEstado(editarAlojamientoDTO.estado());

        alojamientoRepo.save(alojamientoModificado);
        return "El alojamiento ha sido actualizado correctamente";
    }

    @Override
    public String eliminarAlojamiento(String id) throws Exception {
        Optional<Alojamiento> alojamientoOpt = alojamientoRepo.findById(id);
        if(alojamientoOpt.isEmpty()){
            throw new EventoException("No se encontro el alojamiento");
        }else if (alojamientoOpt.get().getEstado().equals(EstadoAlojamiento.INACTIVO)) {
            throw new Exception("El alojamiento ya se encuentra inactivo");
        }
        Alojamiento alojamiento = alojamientoOpt.get();
        alojamiento.setEstado(EstadoAlojamiento.INACTIVO);
        alojamientoRepo.save(alojamiento);
        return id;
    }

    @Override
    public InfoAlojamientoDTO obtenerInformacionAlojamiento(String id) throws Exception {
        Alojamiento alojamiento = alojamientoRepo.findById(id)
                .orElseThrow(() -> new Exception("No se ha encontrado el alojamiento"));
        return new InfoAlojamientoDTO(
                alojamiento.getNombre(),
                alojamiento.getCiudad(),
                alojamiento.getCalificacion(),
                alojamiento.getPrecioNoche(),
                alojamiento.getDistanciaEvento(),
                alojamiento.getTipo()
        );
    }

    @Override
    public List<InfoAlojamientoDTO> listarAlojamientos() {
        List<Alojamiento> alojamientos = alojamientoRepo.findAll();
        List<InfoAlojamientoDTO> respuesta = new ArrayList<>();

        for(Alojamiento alojamiento : alojamientos){
            respuesta.add(new InfoAlojamientoDTO(
                    alojamiento.getNombre(),
                    alojamiento.getCiudad(),
                    alojamiento.getCalificacion(),
                    alojamiento.getPrecioNoche(),
                    alojamiento.getDistanciaEvento(),
                    alojamiento.getTipo()
            ));
        }
        return respuesta;
    }
}
