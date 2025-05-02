package opensuite.crm.offers.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataOfferRepository extends JpaRepository<OfferEntity, Long> {
    Optional<OfferEntity> findOfferByIdOffer(Long idOffer);
}
