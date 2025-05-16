package digitalpyme.crm.offers.domain.repository;

import jakarta.persistence.EntityNotFoundException;
import digitalpyme.crm.offers.domain.Product;
import digitalpyme.crm.offers.infrastructure.repository.CategoryEntity;
import digitalpyme.crm.offers.infrastructure.repository.ProductEntity;
import digitalpyme.crm.offers.infrastructure.repository.SpringDataCategoryRepository;
import digitalpyme.crm.offers.infrastructure.repository.SpringDataProductRepository;
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
