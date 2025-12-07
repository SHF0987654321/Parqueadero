package co.edu.unipacifico.demo.exceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidUserExeception extends RuntimeException {

   public InvalidUserExeception(String message) {
       super(message);
       log.error( message);
   }

}
