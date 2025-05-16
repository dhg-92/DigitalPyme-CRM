package digitalpyme.crm.offers.domain.repository;

import digitalpyme.crm.offers.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findProductById(Long idProduct);

    List<Product> findAllProducts();

    Long createProduct(Product product);

    Boolean removeProduct(Long idProduct);
}
