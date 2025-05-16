package digitalpyme.crm.offers.domain.repository;

import digitalpyme.crm.offers.infrastructure.repository.OfferEntity;
import digitalpyme.crm.offers.infrastructure.repository.OfferProductEntity;
import digitalpyme.crm.offers.infrastructure.repository.SpringDataOfferProductRepository;
import digitalpyme.crm.offers.infrastructure.repository.SpringDataOfferRepository;
import jakarta.persistence.EntityNotFoundException;
import digitalpyme.crm.offers.domain.OfferProduct;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OfferProductRepositoryImpl implements OfferProductRepository {

    private final SpringDataOfferProductRepository jpaRepository;
    private final SpringDataOfferRepository jpaOfferRepository;

    public OfferProductRepositoryImpl(SpringDataOfferProductRepository jpaRepository, SpringDataOfferRepository jpaOfferRepository) {
        this.jpaRepository = jpaRepository;
        this.jpaOfferRepository = jpaOfferRepository;
    }

    @Override
    public Optional<OfferProduct> findOfferProductById(Long id) {
        return jpaRepository.findOfferProductById(id).map(OfferProductEntity::toDomain);
    }

    @Override
    public List<OfferProduct> findOfferProductByOfferId(Long idOffer) {
        return jpaRepository.findOfferProductByOfferId((jpaOfferRepository.findById(idOffer)).get()).stream()
                .map(OfferProductEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean removeOfferProductForIdOffer(Long idOffer) {
        try {
            OfferEntity offerEntity = jpaOfferRepository.findById(idOffer)
                    .orElseThrow(() -> new EntityNotFoundException("Offer not found"));

            jpaRepository.deleteAll(jpaRepository.findOfferProductByOfferId(offerEntity));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean createAllOfferProduct(List<OfferProductEntity> offerProducts) {
       try {
            return jpaRepository.saveAll(offerProducts).size() == offerProducts.size();
        } catch (Exception e) {
            return false;
        }
    }
}
