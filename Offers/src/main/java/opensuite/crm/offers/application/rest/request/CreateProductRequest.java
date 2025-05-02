package opensuite.crm.offers.application.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CreateProductRequest {

        private String name;

        private String description;

        private BigDecimal defaultPrice;

        private BigDecimal defaultTax;

        private String brand;

        private String model;

        private Long categoryId;

}

