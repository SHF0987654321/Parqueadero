package co.edu.unipacifico.demo.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ParameterException extends RuntimeException {

    private String parametro;

    public ParameterException(String message, String parametro) {
        super(message);
        this.parametro = parametro;
        log.error("ParameterException: {} - Parámetro: {}", message, parametro);
    }

}
