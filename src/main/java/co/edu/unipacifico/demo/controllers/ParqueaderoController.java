package co.edu.unipacifico.demo.controllers;

import java.util.Optional;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.unipacifico.demo.dtos.MovimientosDTO;


@RestController
@RequestMapping("/api/parqueadero")
public class ParqueaderoController {

    @GetMapping("/estado")
    public String estadoParqueadero(Model model) {
        model.addAttribute("mensaje", "El parqueadero está operativo");
        return "estadoParqueadero";
    }
}
