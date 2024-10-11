package co.edu.uniquindio.UniEventos.modelo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Localidad {
    private String nombre;
    private Float precio;
    private int entradasVendidas;
    private int aforo;

    public Localidad(String nombre, int capacidadMaxima, float precio) {
        this.nombre = nombre;
        this.aforo = capacidadMaxima;
        this.precio = precio;
    }
}
