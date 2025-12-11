package co.edu.unipacifico.demo.mappers;

import co.edu.unipacifico.demo.dtos.MovimientosDTO;
import co.edu.unipacifico.demo.models.Movimientos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientosMapper {

    @Mapping(source = "usuario.id", target = "usuarioId")
    MovimientosDTO toDTO(Movimientos movimiento);

    // DTO -> Entity: Los objetos relacionados se asignan manualmente en el servicio
    @Mapping(target = "usuario", ignore = true)
    Movimientos toEntity(MovimientosDTO movimientoDTO);
}