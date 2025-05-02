package opensuite.crm.offers.infrastructure.repository;

import jakarta.persistence.*;
import lombok.*;
import opensuite.crm.offers.domain.Product;

import java.math.BigDecimal;

@Entity(name = "Product")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity implements DomainTranslatable<Product> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduct;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "defaultPrice", nullable = false, precision = 10, scale = 2)
    private BigDecimal defaultPrice;

    @Column(name = "defaultTax", nullable = false, precision = 10, scale = 2)
    private BigDecimal defaultTax;

    @Column(name = "brand", nullable = false, length = 100)
    private String brand;

    @Column(name = "model", nullable = false, length = 100)
    private String model;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity categoryId;

    public static ProductEntity fromDomain(Product product, CategoryEntity category) {
        if (product == null || category == null) {
            return null;
        }

        return ProductEntity.builder()
                .idProduct(product.getIdProduct())
                .name(product.getName())
                .description(product.getDescription())
                .defaultPrice(product.getDefaultPrice())
                .defaultTax(product.getDefaultTax())
                .brand(product.getBrand())
                .model(product.getModel())
                .categoryId(category)
                .build();
    }

    @Override
    public Product toDomain() {
        return Product.builder()
                .idProduct(this.getIdProduct())
                .name(this.getName())
                .description(this.getDescription())
                .defaultPrice(this.getDefaultPrice())
                .defaultTax(this.getDefaultTax())
                .brand(this.getBrand())
                .model(this.getModel())
                .category(this.getCategoryId().toDomain())
                .build();
    }
}
