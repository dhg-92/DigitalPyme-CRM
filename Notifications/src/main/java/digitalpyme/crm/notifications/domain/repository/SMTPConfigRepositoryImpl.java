package digitalpyme.crm.notifications.domain.repository;

import digitalpyme.crm.notifications.domain.SmtpConfig;
import digitalpyme.crm.notifications.infrastructure.repository.SmtpConfigEntity;
import digitalpyme.crm.notifications.infrastructure.repository.SpringDataSMTPRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class SMTPConfigRepositoryImpl implements SMTPConfigRepository{

    private final SpringDataSMTPRepository jpaRepository;

    public SMTPConfigRepositoryImpl(SpringDataSMTPRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<SmtpConfig> findSMTPConfig() {
        return jpaRepository.getSMTPConfigById(1L).map(SmtpConfigEntity::toDomain);
    }

    @Override
    public Boolean updateConfig(SmtpConfig smtpConfig) {
        return jpaRepository.save(SmtpConfigEntity.fromDomain(smtpConfig)).getId() != 0;
    }
}
