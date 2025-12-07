package co.edu.unipacifico.demo.mappers;

import co.edu.unipacifico.demo.dtos.MovimientosDTO;
import co.edu.unipacifico.demo.models.Movimientos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientosMapper {

    // Entity -> DTO: Extrae los IDs de las entidades relacionadas
    @Mapping(source = "vehiculo.id", target = "vehiculoId")
    @Mapping(source = "usuario.id", target = "usuarioId")
    @Mapping(source = "lugar.id", target = "lugarId")
    MovimientosDTO toDTO(Movimientos movimiento);

    // DTO -> Entity: Los objetos relacionados se asignan manualmente en el servicio
    @Mapping(target = "vehiculo", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "lugar", ignore = true)
    Movimientos toEntity(MovimientosDTO movimientoDTO);
}