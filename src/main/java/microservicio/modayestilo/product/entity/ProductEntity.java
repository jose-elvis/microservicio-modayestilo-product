package microservicio.modayestilo.product.entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product")
public class ProductEntity extends PanacheEntity {

    private String descripcionCorta;
    private String descripcionLarga;
    private String marca;
    private  String talla;
    private double precio;
    private int stock;
    private Long idCategoria;
    private boolean enabledProduct;
}
