package digitalpyme.crm.offers.domain.repository;

import digitalpyme.crm.offers.domain.Offer;

import java.util.List;
import java.util.Optional;

public interface OfferRepository {
    Optional<Offer> findOfferByIdOffer(Long idOffer);

    List<Offer> findAllOffer();

    Long createOffer(Offer newOffer);

    Long updateOffer(Offer offerRequest);

    Boolean removeOffer(Offer idOffer);
}
