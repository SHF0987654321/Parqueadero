package co.edu.unipacifico.demo.repositories;

import org.springframework.data.jpa.domain.Specification;

import co.edu.unipacifico.demo.models.Vehiculos;

public class VehiculosSpecifications {
    
    private VehiculosSpecifications() {
    };

    public static Specification<Vehiculos> buscarVehiculos(String tipo) {
        return (root, query, criteriaBuilder) -> {
            if (tipo != null && !tipo.isEmpty()) {
                return criteriaBuilder.equal(root.get("tipo"), tipo);
            }
            return criteriaBuilder.conjunction();
        };
    }

}
