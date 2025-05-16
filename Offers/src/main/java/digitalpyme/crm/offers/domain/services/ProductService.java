package digitalpyme.crm.offers.domain.services;

import digitalpyme.crm.offers.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Optional<Product> findProductById(Long idProduct);

    List<Product> findAllProducts();

    Long createProduct(Product product);

    Boolean removeProduct(Long idProduct);
}
