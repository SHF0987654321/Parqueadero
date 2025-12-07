package co.edu.unipacifico.demo.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResouseNotFoundException extends RuntimeException {

    public ResouseNotFoundException(String message) {
        super(message);
        log.error("ResouseNotFoundException: {}", message);
    }

}
