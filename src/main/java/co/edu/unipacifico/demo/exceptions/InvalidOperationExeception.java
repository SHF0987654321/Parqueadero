package co.edu.unipacifico.demo.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidOperationExeception extends RuntimeException {

    public InvalidOperationExeception(String message) {
        super(message);
        log.error("InvalidOperationExeception: {}", message);
    }

}
