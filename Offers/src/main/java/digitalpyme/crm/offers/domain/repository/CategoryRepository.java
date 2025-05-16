package digitalpyme.crm.offers.domain.repository;

import digitalpyme.crm.offers.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findCategoryById(Long idCategory);

    Optional<Category> findCategoryByName(String name);

    Long createCategory(Category newCategory);

    List<Category> findAllCategories();

    Boolean removeCategory(Long idCategory);
    List<Category> findCategoryByParentId(Long parentId);
}
