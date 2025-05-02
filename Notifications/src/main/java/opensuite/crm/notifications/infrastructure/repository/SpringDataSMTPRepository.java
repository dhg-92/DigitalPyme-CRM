package opensuite.crm.notifications.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataSMTPRepository extends JpaRepository<SmtpConfigEntity, Long> {
    Optional<SmtpConfigEntity> getSMTPConfigById(Long id);
}
