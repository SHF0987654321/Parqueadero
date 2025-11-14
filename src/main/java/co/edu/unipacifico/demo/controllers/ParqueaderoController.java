package co.edu.unipacifico.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ParqueaderoController {

    @GetMapping("/detalles_info")
    public String info(Model model) {
        model.addAttribute("Titulo", "Detalles del Parqueadero");
        return "detalles_info";
    }

}
