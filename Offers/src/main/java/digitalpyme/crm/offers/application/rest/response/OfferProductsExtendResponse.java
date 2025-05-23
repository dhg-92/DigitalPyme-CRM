package digitalpyme.crm.offers.application.rest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import digitalpyme.crm.offers.domain.OfferProduct;
import digitalpyme.crm.offers.domain.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class OfferProductsExtendResponse {

    private Long idProduct;

    private String name;

    private String brand;

    private String model;

    private String description;

    private BigDecimal baseproductPrice;

    private BigDecimal productPrice;

    private BigDecimal margin;

    private BigDecimal tax;

    private Long quantity;

    private BigDecimal totalPrice;

    public OfferProductsExtendResponse(Product productInfo, OfferProduct offerProductInfo) {
        this.idProduct = productInfo.getIdProduct();
        this.name = productInfo.getName();
        this.brand = productInfo.getBrand();
        this.model = productInfo.getModel();
        this.description = productInfo.getDescription();
        this.baseproductPrice = productInfo.getDefaultPrice();
        this.margin = offerProductInfo.getMargin();
        this.productPrice = offerProductInfo.getProductPrice().setScale(2, RoundingMode.HALF_UP);
        this.tax = offerProductInfo.getTax();
        this.quantity = offerProductInfo.getQuantity();


        this.totalPrice = offerProductInfo.getProductPrice().add(offerProductInfo.getProductPrice().multiply(offerProductInfo.getTax().divide(BigDecimal.valueOf(100)))).multiply(BigDecimal.valueOf(offerProductInfo.getQuantity()))
                .setScale(2, RoundingMode.HALF_UP);
    }
}

