package opensuite.crm.offers.application.rest.response;

import opensuite.crm.offers.domain.Category;

import java.math.BigDecimal;

public class ProductResponse {
    private Long idProduct;

    private String name;

    private String description;

    private BigDecimal defaultPrice;

    private BigDecimal defaultTax;

    private String brand;

    private String model;

    private Category category;
}
