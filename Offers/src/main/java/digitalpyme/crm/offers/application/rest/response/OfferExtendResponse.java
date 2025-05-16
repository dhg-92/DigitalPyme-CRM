package digitalpyme.crm.offers.application.rest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import digitalpyme.crm.offers.domain.Client;
import digitalpyme.crm.offers.domain.Offer;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class OfferExtendResponse {

    private Long idOffer;

    private String name;

    private Date date;

    private Client client;

    private String status;

    private OfferPrice prices;

    private List<OfferProductsExtendResponse> Items;

    private int totalItems;

    public OfferExtendResponse(Offer offer) {
        this.idOffer = offer.getIdOffer();
        this.name = offer.getName();
        this.date = offer.getDate();
        this.status = offer.getStatus();
        this.client = null;
        this.Items = null;
        this.totalItems = 0;
        this.prices = null;
    }
}
