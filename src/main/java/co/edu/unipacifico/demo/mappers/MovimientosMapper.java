package co.edu.unipacifico.demo.mappers;

import co.edu.unipacifico.demo.dtos.MovimientosRequest;
import co.edu.unipacifico.demo.dtos.MovimientosResponse;
import co.edu.unipacifico.demo.models.Movimientos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientosMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    // Target es el nombre en MovimientosResponse (ahora 'placa', 'nombreLugar', 'tipo')
    @Mapping(source = "vehiculo.placa", target = "placa") // <-- Cambiado
    @Mapping(source = "lugar.nombre", target = "nombreLugar") // <-- Cambiado
    @Mapping(source = "vehiculo.tipo", target = "tipo") // <-- Cambiado
    MovimientosResponse toDTO(Movimientos movimiento);

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "vehiculo", ignore = true) 
    @Mapping(target = "lugar", ignore = true)   
    // El mapeo toEntity funciona automáticamente si los nombres de origen (Request) coinciden
    Movimientos toEntity(MovimientosRequest movimiento);
}