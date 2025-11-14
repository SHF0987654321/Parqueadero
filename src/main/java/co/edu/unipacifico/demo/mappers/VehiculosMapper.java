package co.edu.unipacifico.demo.mappers;

import co.edu.unipacifico.demo.dtos.VehiculosDTO;
import co.edu.unipacifico.demo.models.Vehiculos;

public class VehiculosMapper {
    public static VehiculosDTO toDTO(Vehiculos vehiculo) {
        if (vehiculo == null) {
            return null;
        }
        return new VehiculosDTO(
                vehiculo.getId(),
                vehiculo.getPlaca(),
                vehiculo.getModelo(),
                vehiculo.getMarca(),
                vehiculo.getColor(),
                vehiculo.getTipo()
        );
    }
    public static Vehiculos toEntity(VehiculosDTO vehiculoDTO) {
        if (vehiculoDTO == null) {
            return null;
        }
        Vehiculos vehiculo = new Vehiculos();
        vehiculo.setId(vehiculoDTO.getId());
        vehiculo.setPlaca(vehiculoDTO.getPlaca());
        vehiculo.setModelo(vehiculoDTO.getModelo());
        vehiculo.setMarca(vehiculoDTO.getMarca());
        vehiculo.setColor(vehiculoDTO.getColor());
        vehiculo.setTipo(vehiculoDTO.getTipo());
        return vehiculo;
    }

}
