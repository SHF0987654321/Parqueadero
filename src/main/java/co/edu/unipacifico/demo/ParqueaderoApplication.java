package co.edu.unipacifico.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ParqueaderoApplication {

	public static void main(String[] args) {

		SpringApplication.run(ParqueaderoApplication.class, args);
	}

	@PostConstruct
    public void init() {
        System.out.println("--- VERIFICANDO CONFIGURACIÓN ---");
        System.out.println("DB URL: " + System.getenv("DB_URL"));
        System.out.println("JWT Generator: " + System.getenv("JWT_GENERATOR"));
        System.out.println("---------------------------------");
    }

}
