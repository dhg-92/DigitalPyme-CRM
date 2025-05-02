package opensuite.crm.offers.domain.services;

import opensuite.crm.offers.domain.Offer;

import java.util.List;
import java.util.Optional;

public interface OfferService {
    Optional<Offer> findOfferByIdOffer(Long idOffer);
    List<Offer> findAllOffers();
    Long createOffer(Offer newOffer);
    Long updateOffer(Offer offerRequest);
    Boolean removeOffer(Offer idOffer);
}
