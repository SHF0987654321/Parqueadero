package co.edu.unipacifico.demo.mappers;

import co.edu.unipacifico.demo.dtos.LugaresDTO;
import co.edu.unipacifico.demo.models.Lugares;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LugaresMapper {

    LugaresDTO toDTO(Lugares lugar);

    Lugares toEntity(LugaresDTO lugarDTO);
}
