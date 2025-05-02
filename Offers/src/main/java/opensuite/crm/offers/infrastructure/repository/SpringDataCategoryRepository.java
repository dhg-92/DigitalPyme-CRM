package opensuite.crm.offers.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataCategoryRepository extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findCategoryByIdCategory(Long idCategory);
    Optional<CategoryEntity> findCategoryByName(String name);
    List<CategoryEntity> findCategoryByParentId(Long parentId);
}
