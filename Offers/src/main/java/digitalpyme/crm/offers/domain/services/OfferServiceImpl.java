package digitalpyme.crm.offers.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import digitalpyme.crm.offers.domain.Offer;
import digitalpyme.crm.offers.domain.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;

    @Override
    public Optional<Offer> findOfferByIdOffer(Long idOffer) {
        return offerRepository.findOfferByIdOffer(idOffer);
    }

    @Override
    public List<Offer> findAllOffers() {
        return offerRepository.findAllOffer();
    }

    @Override
    public Long createOffer(Offer newOffer) {
        return offerRepository.createOffer(newOffer);
    }

    @Override
    public Long updateOffer(Offer offerRequest) {
        return offerRepository.updateOffer(offerRequest);
    }

    @Override
    public Boolean removeOffer(Offer offer) {
        return offerRepository.removeOffer(offer);
    }
}
