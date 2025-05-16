package digitalpyme.crm.offers.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataOfferProductRepository extends JpaRepository<OfferProductEntity, Long> {
    Optional<OfferProductEntity> findOfferProductById(Long id);
    List<OfferProductEntity> findOfferProductByOfferId(OfferEntity offer);
}
