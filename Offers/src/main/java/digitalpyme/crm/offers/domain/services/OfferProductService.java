package digitalpyme.crm.offers.domain.services;

import digitalpyme.crm.offers.domain.OfferProduct;
import digitalpyme.crm.offers.infrastructure.repository.OfferProductEntity;

import java.util.List;
import java.util.Optional;

public interface OfferProductService {
    Optional<OfferProduct> findOfferProductById(Long id);

    List<OfferProduct> findOfferProductByOfferId(Long idOffer);

    Boolean removeOfferProductForIdOffer(Long idOfferProduct);

    Boolean createAllOfferProduct(List<OfferProductEntity> offerProducts);

}
