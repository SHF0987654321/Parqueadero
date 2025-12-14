package co.edu.unipacifico.demo.mappers;

import co.edu.unipacifico.demo.dtos.LugaresRequest;
import co.edu.unipacifico.demo.dtos.LugaresResponse;
import co.edu.unipacifico.demo.models.Lugares;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LugaresMapper {

    LugaresResponse toDTO(Lugares lugar);

    Lugares toEntity(LugaresRequest lugar);
}
