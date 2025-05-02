package opensuite.crm.offers.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import opensuite.crm.offers.domain.Product;
import opensuite.crm.offers.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Optional<Product> findProductById(Long idProduct) {
        return productRepository.findProductById(idProduct);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAllProducts();
    }

    @Override
    public Long createProduct(Product product) {
        return productRepository.createProduct(product);
    }

    @Override
    public Boolean removeProduct(Long idProduct) {
        return productRepository.removeProduct(idProduct);
    }
}
