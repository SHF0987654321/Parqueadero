package co.edu.unipacifico.demo.configs;

import co.edu.unipacifico.demo.models.Permisos;
import co.edu.unipacifico.demo.models.Roles;
import co.edu.unipacifico.demo.models.Usuarios;
import co.edu.unipacifico.demo.repositories.PermisosRepository;
import co.edu.unipacifico.demo.repositories.RolesRepository;
import co.edu.unipacifico.demo.repositories.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * Esta clase inicializa los datos básicos del sistema:
 * - Permisos
 * - Roles (USER, ADMIN)
 * - Usuario administrador por defecto
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PermisosRepository permisosRepository;
    private final RolesRepository rolesRepository;
    private final UsuariosRepository usuariosRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        // 1. Crear permisos si no existen
        Permisos verEstadisticas = crearPermisoSiNoExiste("VER_ESTADISTICAS");
        Permisos gestionarVehiculos = crearPermisoSiNoExiste("GESTIONAR_VEHICULOS");
        Permisos gestionarMovimientos = crearPermisoSiNoExiste("GESTIONAR_MOVIMIENTOS");
        Permisos gestionarLugares = crearPermisoSiNoExiste("GESTIONAR_LUGARES");
        Permisos gestionarUsuarios = crearPermisoSiNoExiste("GESTIONAR_USUARIOS");

        // 2. Crear rol USER si no existe
        Roles rolUser = rolesRepository.findByNombre("USER").orElse(null);
        if (rolUser == null) {
            rolUser = new Roles();
            rolUser.setNombre("USER");
            rolUser.setPermisos(new HashSet<>(Set.of(verEstadisticas)));
            rolUser = rolesRepository.save(rolUser);
            System.out.println("✓ Rol USER creado");
        }

        // 3. Crear rol ADMIN si no existe
        Roles rolAdmin = rolesRepository.findByNombre("ADMIN").orElse(null);
        if (rolAdmin == null) {
            rolAdmin = new Roles();
            rolAdmin.setNombre("ADMIN");
            rolAdmin.setPermisos(new HashSet<>(Set.of(
                verEstadisticas,
                gestionarVehiculos,
                gestionarMovimientos,
                gestionarLugares,
                gestionarUsuarios
            )));
            rolAdmin = rolesRepository.save(rolAdmin);
            System.out.println("✓ Rol ADMIN creado");
        }

        // 4. Crear usuario administrador por defecto si no existe
        if (usuariosRepository.findByNombre("admin").isEmpty()) {
            // Recargar el rol desde la base de datos para asegurar que está managed
            Roles adminRolManaged = rolesRepository.findByNombre("ADMIN")
                .orElseThrow(() -> new RuntimeException("Rol ADMIN no encontrado"));
            
            Usuarios admin = new Usuarios();
            admin.setNombre("admin");
            admin.setClave(passwordEncoder.encode("admin123"));
            admin.setRoles(new HashSet<>(Set.of(adminRolManaged)));
            
            usuariosRepository.save(admin);
            
            System.out.println("✓ Usuario administrador creado:");
            System.out.println("  - Usuario: admin");
            System.out.println("  - Contraseña: admin123");
        }

        // 5. Crear usuario USER de prueba si no existe
        if (usuariosRepository.findByNombre("usuario").isEmpty()) {
            // Recargar el rol desde la base de datos para asegurar que está managed
            Roles userRolManaged = rolesRepository.findByNombre("USER")
                .orElseThrow(() -> new RuntimeException("Rol USER no encontrado"));
            
            Usuarios usuario = new Usuarios();
            usuario.setNombre("usuario");
            usuario.setClave(passwordEncoder.encode("usuario123"));
            usuario.setRoles(new HashSet<>(Set.of(userRolManaged)));
            
            usuariosRepository.save(usuario);
            
            System.out.println("✓ Usuario de prueba creado:");
            System.out.println("  - Usuario: usuario");
            System.out.println("  - Contraseña: usuario123");
        }
    }

    private Permisos crearPermisoSiNoExiste(String nombre) {
        return permisosRepository.findAll()
            .stream()
            .filter(p -> p.getNombre().equals(nombre))
            .findFirst()
            .orElseGet(() -> {
                Permisos permiso = new Permisos(nombre);
                return permisosRepository.save(permiso);
            });
    }
}
