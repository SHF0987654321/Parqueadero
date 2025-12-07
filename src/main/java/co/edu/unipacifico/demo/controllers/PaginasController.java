package co.edu.unipacifico.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginasController {

    // Ruta raíz redirige al login
    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    // Página de login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Página de registro
    @GetMapping("/registrar")
    public String registrar() {
        return "registrar";
    }

    // Página de inicio (si la tienes)
    @GetMapping("/inicio")
    public String inicio() {
        return "inicio";
    }

    // Dashboard/Perfil (requiere autenticación)
    @GetMapping({"/dashboard", "/perfil"})
    public String dashboard() {
        return "perfil";
    }

    // Configuración (requiere autenticación)
    @GetMapping("/configuracion")
    public String configuracion() {
        return "configuracion";
    }
}
