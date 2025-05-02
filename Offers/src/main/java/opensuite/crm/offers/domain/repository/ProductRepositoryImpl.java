package opensuite.crm.offers.domain.repository;

import jakarta.persistence.EntityNotFoundException;
import opensuite.crm.offers.domain.Product;
import opensuite.crm.offers.infrastructure.repository.CategoryEntity;
import opensuite.crm.offers.infrastructure.repository.ProductEntity;
import opensuite.crm.offers.infrastructure.repository.SpringDataCategoryRepository;
import opensuite.crm.offers.infrastructure.repository.SpringDataProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final SpringDataProductRepository jpaRepository;
    private final SpringDataCategoryRepository jpaCategoryRepository;

    public ProductRepositoryImpl(SpringDataProductRepository jpaRepository, SpringDataCategoryRepository jpaCategoryRepository) {
        this.jpaRepository = jpaRepository;
        this.jpaCategoryRepository = jpaCategoryRepository;
    }

    @Override
    public Optional<Product> findProductById(Long idClient) {
        return jpaRepository.findProductByIdProduct(idClient).map(ProductEntity::toDomain);
    }

    @Override
    public List<Product> findAllProducts() {
        return jpaRepository.findAll().stream().map(ProductEntity::toDomain).collect(Collectors.toList());
    }

    @Override
    public Long createProduct(Product product) {
        CategoryEntity category = jpaCategoryRepository.findCategoryByIdCategory(product.getCategory().getIdCategory())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return jpaRepository.save(ProductEntity.fromDomain(product, category)).getIdProduct();
    }

    @Override
    public Boolean removeProduct(Long idProduct) {
        try {
            jpaRepository.delete(jpaRepository.findById(idProduct).get());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
