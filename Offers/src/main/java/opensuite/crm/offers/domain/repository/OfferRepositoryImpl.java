package opensuite.crm.offers.domain.repository;

import jakarta.persistence.EntityNotFoundException;
import opensuite.crm.offers.domain.Offer;
import opensuite.crm.offers.infrastructure.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OfferRepositoryImpl implements OfferRepository {

    private final SpringDataOfferRepository jpaRepository;
    private final SpringDataClientRepository jpaClientRepository;
    private final SpringDataOfferProductRepository jpaOfferProductRepository;

    public OfferRepositoryImpl(SpringDataOfferRepository jpaRepository, SpringDataClientRepository jpaClientRepository, SpringDataOfferProductRepository jpaOfferProductRepository) {
        this.jpaRepository = jpaRepository;
        this.jpaClientRepository = jpaClientRepository;
        this.jpaOfferProductRepository = jpaOfferProductRepository;
    }

    @Override
    public Optional<Offer> findOfferByIdOffer(Long idOffer) {
        return jpaRepository.findOfferByIdOffer(idOffer).map(OfferEntity::toDomain);
    }

    @Override
    public List<Offer> findAllOffer() {
        return jpaRepository.findAll().stream().map(OfferEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Long createOffer(Offer newOffer) {
        ClientEntity clientEntity = jpaClientRepository.findById(newOffer.getClient().getIdClient())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        return jpaRepository.save(OfferEntity.fromDomain(newOffer, clientEntity)).getIdOffer();
    }

    @Override
    public Long updateOffer(Offer offerRequest) {
        ClientEntity clientEntity = jpaClientRepository.findById(offerRequest.getClient().getIdClient())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        return jpaRepository.save(OfferEntity.fromDomain(offerRequest, clientEntity)).getIdOffer();
    }

    @Override
    public Boolean removeOffer(Offer offer) {
        List<OfferProductEntity> allOfferProductEntity = jpaOfferProductRepository.findOfferProductByOfferId((jpaRepository.findById(offer.getIdOffer())).get());

        ClientEntity clientEntity = jpaClientRepository.findById(offer.getClient().getIdClient())
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        try {
            jpaOfferProductRepository.deleteAll(allOfferProductEntity);
            jpaRepository.delete(OfferEntity.fromDomain(offer, clientEntity));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
