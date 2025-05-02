package opensuite.crm.offers.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import opensuite.crm.offers.domain.OfferProduct;
import opensuite.crm.offers.domain.repository.OfferProductRepository;
import opensuite.crm.offers.infrastructure.repository.OfferProductEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class OfferProductServiceImpl implements OfferProductService {

    private final OfferProductRepository offerProductRepository;

    @Override
    public Optional<OfferProduct> findOfferProductById(Long id) {
        return offerProductRepository.findOfferProductById(id);
    }

    @Override
    public List<OfferProduct> findOfferProductByOfferId(Long idOffer) {
        return offerProductRepository.findOfferProductByOfferId(idOffer);
    }

    @Override
    public Boolean removeOfferProductForIdOffer(Long idOfferProduct) {
        return offerProductRepository.removeOfferProductForIdOffer(idOfferProduct);
    }

    @Override
    public Boolean createAllOfferProduct(List<OfferProductEntity> offerProducts) {
        return offerProductRepository.createAllOfferProduct(offerProducts);
    }
}
