package co.edu.uniquindio.UniEventos.servicios.implementaciones;

import co.edu.uniquindio.UniEventos.dto.EventoDTO.*;
import co.edu.uniquindio.UniEventos.excepciones.EventoException;
import co.edu.uniquindio.UniEventos.modelo.EstadoEvento;
import co.edu.uniquindio.UniEventos.modelo.Evento;
import co.edu.uniquindio.UniEventos.modelo.Localidad;
import co.edu.uniquindio.UniEventos.modelo.TipoEvento;
import co.edu.uniquindio.UniEventos.repositorios.EventoRepo;
import co.edu.uniquindio.UniEventos.servicios.interfaces.EventoServicio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class EventoServicioImpl implements EventoServicio {
    private final EventoRepo eventoRepo;
    public String crearEvento(CrearEventoDTO crearEventoDTO) throws EventoException {
        if (existeNombre(crearEventoDTO.nombre())) {
            throw new EventoException("El nombre ya existe, elija otro nombre");
        }
        Evento evento = new Evento();
        evento.setNombre(crearEventoDTO.nombre());
        evento.setDescripcion(crearEventoDTO.descripcion());
        evento.setDireccion(crearEventoDTO.direccion());
        evento.setCiudad(crearEventoDTO.ciudad());
        evento.setImagenPortada(crearEventoDTO.imagenPortada());
        evento.setImagenLocalidad(crearEventoDTO.imagenLocalidad());
        evento.setTipo(crearEventoDTO.tipo());
        evento.setFecha(crearEventoDTO.fecha());
        List<Localidad> localidades = crearLocalidades(crearEventoDTO.localidades());
        evento.setLocalidades(localidades);
        Evento eventoCreado = eventoRepo.save(evento);
        return eventoCreado.getId();
    }

    private List<Localidad> crearLocalidades(List<LocalidadDTO> listaLocalidadesDTO) {
        List<Localidad> localidades = new ArrayList<>(listaLocalidadesDTO.size());
        for (LocalidadDTO crearLocalidadDTO : listaLocalidadesDTO) {
            Localidad localidad = new Localidad(
                    crearLocalidadDTO.nombre(),
                    crearLocalidadDTO.aforo(),
                    crearLocalidadDTO.precio()
            );
            localidades.add(localidad);
        }
        return localidades;
    }

    @Override
    public String editarEvento(EditarEventoDTO editarEventoDTO) throws EventoException {
        Optional<Evento> optionalEvento = eventoRepo.findById(editarEventoDTO.id());
        if (optionalEvento.isEmpty()) {
            throw new EventoException("No existe este evento");
        }
        Evento eventoModificado = optionalEvento.get();
        eventoModificado.setImagenPortada(editarEventoDTO.imagenPoster());
        eventoModificado.setNombre(editarEventoDTO.nombre());
        eventoModificado.setDescripcion(editarEventoDTO.descripcion());
        eventoModificado.setDireccion(editarEventoDTO.direccion());
        eventoModificado.setImagenLocalidad(editarEventoDTO.imagenLocalidades());
        eventoModificado.setTipo(editarEventoDTO.tipo());
        eventoModificado.setEstado(editarEventoDTO.estado());
        eventoModificado.setFecha(editarEventoDTO.fecha());
        eventoModificado.setCiudad(editarEventoDTO.ciudad());

        List<Localidad> localidadesActualizadas = modificarLocalidades(eventoModificado.getLocalidades(),editarEventoDTO.localidades());
        eventoModificado.setLocalidades(localidadesActualizadas);

        eventoRepo.save(eventoModificado);
        return "El evento ha sido actualizado correctamente";
    }

    private List<Localidad> modificarLocalidades(List<Localidad> localidadesActuales ,List<LocalidadDTO> listaLocalidadesDTO) throws EventoException {

        List<Localidad> localidadesActualizadas = new ArrayList<>(localidadesActuales);
        if(!localidadesActuales.isEmpty()) {
            for (LocalidadDTO localidadDTO : listaLocalidadesDTO) {
                for (Localidad localidad : localidadesActuales) {
                    if (localidad.getNombre().equals(localidadDTO.nombre())) {
                        localidad.setNombre(localidadDTO.nombre());
                        localidad.setAforo(localidadDTO.aforo());
                        localidad.setPrecio(localidadDTO.precio());
                    }
                }
            }
        }else{
            throw new EventoException("La localidad que intentas editar no existe");
        }
        return localidadesActualizadas;
    }

    @Override
    public String eliminarEvento(String id) throws EventoException {
        Optional<Evento> optionalEvento = eventoRepo.findById(id);
        if(optionalEvento.isEmpty()){
            throw new EventoException("No se encontro el evento");
        }else if (optionalEvento.get().getEstado().equals(EstadoEvento.INACTIVO)) {
            throw new EventoException("El evento ya se encuentra inactivo");
        }
        Evento evento = optionalEvento.get();
        evento.setEstado(EstadoEvento.INACTIVO);
        eventoRepo.save(evento);
        return id;
    }

    @Override
    public InfoEventoDTO obtenerInformacionEvento(String nombre) throws EventoException {
        Evento evento = eventoRepo.findByNombre(nombre)
                .orElseThrow(() -> new EventoException("No se ha encontrado el evento"));
        return new InfoEventoDTO(
                evento.getImagenPortada(),
                evento.getImagenLocalidad(),
                evento.getNombre(),
                evento.getDescripcion(),
                evento.getDireccion(),
                evento.getTipo(),
                evento.getFecha(),
                evento.getCiudad(),
                evento.getLocalidades()
        );
    }

    @Override
    public List<ItemEventoDTO> listarEventos() {
        List<Evento> eventos = eventoRepo.findAll();
        List<ItemEventoDTO> respuesta = new ArrayList<>();

        for(Evento evento : eventos){
            respuesta.add(new ItemEventoDTO(
                    evento.getImagenPortada(),
                    evento.getNombre(),
                    evento.getFecha(),
                    evento.getDireccion(),
                    evento.getTipo()
            ));
        }
        return respuesta;
    }

    @Override
    public List<String> listarTipos() {
        // Convierte los valores de la enumeración a una lista de cadenas
        return Arrays.stream(TipoEvento.values())
                .map(Enum::name) // Convierte cada valor del enum a su representación en String
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemEventoDTO> filtrarEventos(FiltroEventoDTO filtroEventoDTO) {
        List<Evento> eventos = new ArrayList<>();

        String nombre = filtroEventoDTO.nombre();
        TipoEvento tipoEvento = filtroEventoDTO.tipoEvento();
        String ciudad = filtroEventoDTO.ciudad();

        if (nombre != null && tipoEvento == null && ciudad == null) {
            eventos = eventoRepo.findByNombreRegex(nombre);
        } else if (nombre == null && tipoEvento != null && ciudad == null) {
            eventos = eventoRepo.findByTipo(tipoEvento);
        } else if (nombre == null && tipoEvento == null && ciudad != null) {
            eventos = eventoRepo.findByCiudadRegex(ciudad);
        } else if (nombre != null && tipoEvento != null && ciudad == null) {
            eventos = eventoRepo.findByNombreRegexAndTipo(nombre, tipoEvento.toString());
        } else if (nombre != null && tipoEvento == null && ciudad != null) {
            eventos = eventoRepo.findByNombreRegexAndCiudadRegex(nombre, ciudad);
        } else if (nombre == null && tipoEvento != null && ciudad != null) {
            eventos = eventoRepo.findByTipoAndCiudadRegex(tipoEvento.toString(), ciudad);
        } else if (nombre != null && tipoEvento != null && ciudad != null) {
            eventos = eventoRepo.findByNombreRegexAndTipoAndCiudadRegex(nombre, tipoEvento.toString(), ciudad);
        }

        // Conversión de la lista de eventos a DTOs
        List<ItemEventoDTO> respuesta = eventos.stream().map(evento -> new ItemEventoDTO(
                evento.getImagenPortada(),
                evento.getNombre(),
                evento.getFecha(),
                evento.getDireccion(),
                evento.getTipo()
        )).collect(Collectors.toList());

        return respuesta;
    }



    @Override
    public List<ItemEventoDTO> filtrarPorTipo(TipoEvento tipoEvento) {
        List<Evento> eventos = eventoRepo.findByTipo(tipoEvento);
        if (eventos.isEmpty()) {
            throw new EventoException("No se encontraron eventos para el tipo de evento: " + tipoEvento);
        }
        List<ItemEventoDTO> eventoFiltradoDTO = new ArrayList<>();

        for(Evento evento : eventos){
            eventoFiltradoDTO.add(new ItemEventoDTO(
                    evento.getImagenPortada(),
                    evento.getNombre(),
                    evento.getFecha(),
                    evento.getDireccion(),
                    evento.getTipo()
            ));
        }
        return eventoFiltradoDTO;
    }


    @Override
    public List<ItemEventoDTO> filtrarPorFecha(LocalDate fecha) {
        List<Evento> eventos = eventoRepo.buscarPorRangoDeFechas(fecha.atStartOfDay(), fecha.plusDays(1).atStartOfDay());
        if (eventos.isEmpty()) {
            throw new EventoException("No se encontraron eventos para la fecha: " + fecha);
        }
        List<ItemEventoDTO> eventoFiltradoDTO = new ArrayList<>();
        for(Evento evento : eventos){
            eventoFiltradoDTO.add(new ItemEventoDTO(
                    evento.getImagenPortada(),
                    evento.getNombre(),
                    evento.getFecha(),
                    evento.getDireccion(),
                    evento.getTipo()
            ));
        }
        return eventoFiltradoDTO;
    }

    @Override
    public List<ItemEventoDTO> filtrarPorCiudad(String ciudad) {
        List<Evento> eventos = eventoRepo.findByCiudad(ciudad);
        if (eventos.isEmpty()) {
            throw new EventoException("No se encontraron eventos para la ciudad: " + ciudad);
        }
        List<ItemEventoDTO> eventoFiltradoDTO = new ArrayList<>();
        for(Evento evento : eventos){
            eventoFiltradoDTO.add(new ItemEventoDTO(
                    evento.getImagenPortada(),
                    evento.getNombre(),
                    evento.getFecha(),
                    evento.getDireccion(),
                    evento.getTipo()
            ));
        }
        return eventoFiltradoDTO;
    }

    @Override
    public List<ItemEventoDTO> filtrarPorAlojamientosCercanos(String ciudad, String direccion) {
        List<Evento> eventos = eventoRepo.buscarConAlojamientosCercanos(ciudad, direccion);
        if (eventos.isEmpty()) {
            throw new EventoException("No se encontraron alojamientos cercanos al evento");
        }
        List<ItemEventoDTO> eventoFiltradoDTO = new ArrayList<>();
        for(Evento evento : eventos){
            eventoFiltradoDTO.add(new ItemEventoDTO(
                    evento.getImagenPortada(),
                    evento.getNombre(),
                    evento.getFecha(),
                    evento.getDireccion(),
                    evento.getTipo()
            ));
        }
        return eventoFiltradoDTO;
    }

    @Override
    public List<ItemEventoDTO> filtrarPorRangoDeFechas(LocalDateTime desde, LocalDateTime hasta) {
        List<Evento> eventos = eventoRepo.buscarPorRangoDeFechas(desde, hasta);
        if (eventos.isEmpty()) {
            throw new EventoException("No se encontraron eventos en el rango de fechas: " + desde + " a " + hasta);
        }
        List<ItemEventoDTO> eventoFiltradoDTO = new ArrayList<>();
        for(Evento evento : eventos){
            eventoFiltradoDTO.add(new ItemEventoDTO(
                    evento.getImagenPortada(),
                    evento.getNombre(),
                    evento.getFecha(),
                    evento.getDireccion(),
                    evento.getTipo()
            ));
        }
        return eventoFiltradoDTO;
    }
    @Override
    public Evento obtenerEvento(String id) throws EventoException {
        return eventoRepo.findById(id).orElseThrow( () -> new EventoException("El evento no existe") );
    }

    @Override
    public List<Evento> getAll() {
        return eventoRepo.findAll();
    }

    public boolean existeNombre(String nombre){
        return eventoRepo.findByNombre(nombre).isPresent();
    }

    @Override
    public void actualizarCapacidadLocalidad(Evento evento, String nombreLocalidad, int entradasVendidas) throws Exception {
        Localidad localidad = evento.getLocalidades().stream().filter(l -> l.getNombre().equals(nombreLocalidad) ).findFirst().orElse(null);
        if(localidad == null){
            throw new Exception("No existe la localidad");
        }
        localidad.setEntradasVendidas(localidad.getEntradasVendidas()+entradasVendidas );
        eventoRepo.save(evento);

    }

}
