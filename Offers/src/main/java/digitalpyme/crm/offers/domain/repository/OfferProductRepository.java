package digitalpyme.crm.offers.domain.repository;

import digitalpyme.crm.offers.domain.OfferProduct;
import digitalpyme.crm.offers.infrastructure.repository.OfferProductEntity;

import java.util.List;
import java.util.Optional;

public interface OfferProductRepository {
    Optional<OfferProduct> findOfferProductById(Long id);

    List<OfferProduct> findOfferProductByOfferId(Long idOffer);

    Boolean createAllOfferProduct(List<OfferProductEntity> offerProducts);

    Boolean removeOfferProductForIdOffer(Long idOffer);
}
