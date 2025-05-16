package digitalpyme.crm.offers.infrastructure.repository;

import jakarta.persistence.*;
import lombok.*;
import digitalpyme.crm.offers.domain.Offer;

import java.math.BigDecimal;
import java.util.Date;

@Entity(name = "Offer")
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferEntity implements DomainTranslatable<Offer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOffer;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "date", nullable = false)
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clientId", nullable = false)
    private ClientEntity clientId;

    @Column(name = "status", nullable = false, length = 50)
    private String status;

    @Column(name = "totalPrice", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;


    public static OfferEntity fromDomain(Offer offer, ClientEntity client) {
        if (offer == null || client == null) {
            return null;
        }

        return OfferEntity.builder()
                .idOffer(offer.getIdOffer())
                .name(offer.getName())
                .date(offer.getDate())
                .clientId(client)
                .status(offer.getStatus())
                .totalPrice(offer.getTotalPrice())
                .build();
    }

    @Override
    public Offer toDomain() {
        return Offer.builder()
                .idOffer(this.getIdOffer())
                .name(this.getName())
                .date(this.getDate())
                .client(this.getClientId().toDomain())
                .status(this.getStatus())
                .totalPrice(this.getTotalPrice())
                .build();
    }

}
