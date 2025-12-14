package co.edu.unipacifico.demo.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroresResponse {

    private String codigo, mensaje;
    private LocalDateTime hora;

}
