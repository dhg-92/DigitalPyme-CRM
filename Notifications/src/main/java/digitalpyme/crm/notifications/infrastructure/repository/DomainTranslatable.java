package digitalpyme.crm.notifications.infrastructure.repository;

public interface DomainTranslatable<T> {
    T toDomain();
}
