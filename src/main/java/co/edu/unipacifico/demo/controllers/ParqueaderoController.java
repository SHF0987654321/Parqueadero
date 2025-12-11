package co.edu.unipacifico.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/parqueadero")
public class ParqueaderoController {

    @GetMapping("/estado")
    public ResponseEntity<String> estadoParqueadero() {
        return ResponseEntity.ok("El parqueadero está operativo");
    }
}
