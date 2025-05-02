package opensuite.crm.offers.application.rest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import opensuite.crm.offers.domain.Client;
import opensuite.crm.offers.domain.Offer;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class OfferResponse {

    private Long idOffer;

    private String name;

    private Date date;

    private Client client;

    private String status;

    private BigDecimal totalPrice;

    public static OfferResponse offerToGetOfferResponse(Offer offer) {
        return new OfferResponse(
                offer.getIdOffer(),
                offer.getName(),
                offer.getDate(),
                offer.getClient(),
                offer.getStatus(),
                offer.getTotalPrice()
        );
    }
}
