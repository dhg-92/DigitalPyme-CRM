package digitalpyme.crm.offers.infrastructure.repository;

import jakarta.persistence.*;
import lombok.*;
import digitalpyme.crm.offers.domain.OfferProduct;

import java.math.BigDecimal;

@Entity(name = "OfferProduct")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferProductEntity implements DomainTranslatable<OfferProduct> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offer_id", nullable = false)
    private OfferEntity offerId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity productId;

    @Column(name = "baseproductPrice", nullable = false, precision = 10, scale = 2)
    private BigDecimal baseproductPrice;

    @Column(name = "productPrice", nullable = false, precision = 10, scale = 2)
    private BigDecimal productPrice;

    @Column(name = "margin", nullable = false, precision = 10, scale = 2)
    private BigDecimal margin;

    @Column(name = "tax", nullable = false, precision = 10, scale = 2)
    private BigDecimal tax;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    public static OfferProductEntity fromDomain(OfferProduct offerProduct, OfferEntity offer, ProductEntity product) {
        if (offerProduct == null || offer == null || product == null) {
            return null;
        }

        return OfferProductEntity.builder()
                .id(offerProduct.getId())
                .offerId(offer)
                .productId(product)
                .baseproductPrice(offerProduct.getBaseproductPrice())
                .productPrice(offerProduct.getProductPrice())
                .tax(offerProduct.getTax())
                .quantity(offerProduct.getQuantity())
                .margin(offerProduct.getMargin())
                .build();
    }

    @Override
    public OfferProduct toDomain() {
        return OfferProduct.builder()
                .id(this.getId())
                .offerId(this.getOfferId().getIdOffer())
                .productId(this.getProductId().getIdProduct())
                .baseproductPrice(this.getBaseproductPrice())
                .productPrice(this.getProductPrice())
                .tax(this.getTax())
                .quantity(this.getQuantity())
                .margin(this.getMargin())
                .build();
    }
}
