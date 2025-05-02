package opensuite.crm.offers.domain.repository;

import opensuite.crm.offers.domain.Category;
import opensuite.crm.offers.infrastructure.repository.CategoryEntity;
import opensuite.crm.offers.infrastructure.repository.SpringDataCategoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final SpringDataCategoryRepository jpaRepository;

    public CategoryRepositoryImpl(SpringDataCategoryRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Category> findCategoryById(Long idClient) {
        return jpaRepository.findCategoryByIdCategory(idClient).map(CategoryEntity::toDomain);
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        return jpaRepository.findCategoryByName(name).map(CategoryEntity::toDomain);
    }

    @Override
    public Long createCategory(Category newCategory) {
        return jpaRepository.save(CategoryEntity.fromDomain(newCategory)).getIdCategory();
    }

    @Override
    public List<Category> findAllCategories() {
        return jpaRepository.findAll().stream().map(CategoryEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Boolean removeCategory(Long idCategory) {
        try {
            jpaRepository.delete(jpaRepository.findById(idCategory).orElseThrow(IllegalArgumentException::new));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Category> findCategoryByParentId(Long parentId) {
        return jpaRepository.findCategoryByParentId(parentId)
                .stream()
                .map(CategoryEntity::toDomain)
                .collect(Collectors.toList());
    }
}
