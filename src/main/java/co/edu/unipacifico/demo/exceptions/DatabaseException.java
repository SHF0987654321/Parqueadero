package co.edu.unipacifico.demo.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Exception cause) {
        super(message);
        log.error(message);
        log.error("DatabaseException: {}", cause.getMessage());
        if(cause != null) {
            log.error("Caused by: {}", cause.getCause().getMessage());
        }
    }

}
