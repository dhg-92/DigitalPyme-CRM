package digitalpyme.crm.offers.domain.services;

import digitalpyme.crm.offers.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> findCategoryById(Long idCategory);

    Optional<Category> findCategoryByName(String name);

    Long createCategory(Category newCategory);

    List<Category> findAllCategories();

    Boolean removeCategory(Long id);

    List<Category> findCategoryByParentId(Long idCategory);
}
