package co.edu.unipacifico.demo.exceptions;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RequestException extends RuntimeException {

    private String codigo;

    public RequestException(String message, String codigo) {
        super(message);
        this.codigo = codigo;
    }

}
