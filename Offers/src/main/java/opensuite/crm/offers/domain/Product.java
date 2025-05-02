package opensuite.crm.offers.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProduct;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private BigDecimal defaultPrice;

    @NotNull
    private BigDecimal defaultTax;

    @NotNull
    private String brand;

    @NotNull
    private String model;

    @NotNull
    private Category category;
}
