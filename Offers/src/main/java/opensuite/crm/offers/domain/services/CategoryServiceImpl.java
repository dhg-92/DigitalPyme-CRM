package opensuite.crm.offers.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import opensuite.crm.offers.domain.Category;
import opensuite.crm.offers.domain.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Optional<Category> findCategoryById(Long idCategory) {
        return categoryRepository.findCategoryById(idCategory);
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name);
    }

    @Override
    public Long createCategory(Category newCategory) {
        return categoryRepository.createCategory(newCategory);
    }

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAllCategories();
    }

    @Override
    public Boolean removeCategory(Long id) {
        return categoryRepository.removeCategory(id);
    }

    @Override
    public List<Category> findCategoryByParentId(Long idCategory) {
        return categoryRepository.findCategoryByParentId(idCategory);
    }

}
