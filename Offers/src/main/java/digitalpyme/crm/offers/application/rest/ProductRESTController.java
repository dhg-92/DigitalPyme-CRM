package digitalpyme.crm.offers.application.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import digitalpyme.crm.offers.application.rest.request.CreateProductRequest;
import digitalpyme.crm.offers.domain.Category;
import digitalpyme.crm.offers.domain.Product;
import digitalpyme.crm.offers.domain.services.CategoryService;
import digitalpyme.crm.offers.domain.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/products")
public class ProductRESTController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllProducts() {
        log.trace("getAllProducts");

        return productService.findAllProducts().stream().toList();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid CreateProductRequest createProductRequest) {
        log.trace("createProduct");

        Optional<Category> existingCategory = categoryService.findCategoryById(createProductRequest.getCategoryId());

        if (existingCategory.isEmpty()) {
            log.trace("Category not exist " + createProductRequest);
            return ResponseEntity.status(409).body("Category not Exists.");
        }

        Product newProduct = new Product();
        newProduct.setName(createProductRequest.getName());
        newProduct.setDescription(createProductRequest.getDescription());
        newProduct.setDefaultPrice(createProductRequest.getDefaultPrice());
        newProduct.setDefaultTax(createProductRequest.getDefaultTax());
        newProduct.setBrand(createProductRequest.getBrand());
        newProduct.setModel(createProductRequest.getModel());
        newProduct.setCategory(existingCategory.get());

        Long productId = productService.createProduct(newProduct);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productId)
                .toUri();
        return ResponseEntity.created(uri).body(productId);
    }

    @PutMapping("/{idProduct}")
    public ResponseEntity<?> updateProduct(@RequestBody @Valid CreateProductRequest updateProductRequest, @PathVariable Long idProduct) {
        log.trace("updateProduct");

        Optional<Product> existingProduct = productService.findProductById(idProduct);

        if (existingProduct.isEmpty()) {
            log.trace("Product not exist " + updateProductRequest);
            return ResponseEntity.status(409).body("Product not Exists.");
        }
        Optional<Category> existingCategory = categoryService.findCategoryById(updateProductRequest.getCategoryId());

        if (existingCategory.isEmpty()) {
            log.trace("Category not exist " + updateProductRequest);
            return ResponseEntity.status(409).body("Category not Exists.");
        }

        log.trace("Update client " + updateProductRequest);

        Product updateProduct = existingProduct.get();
        updateProduct.setName(updateProductRequest.getName());
        updateProduct.setDescription(updateProductRequest.getDescription());
        updateProduct.setDefaultPrice(updateProductRequest.getDefaultPrice());
        updateProduct.setDefaultTax(updateProductRequest.getDefaultTax());
        updateProduct.setBrand(updateProductRequest.getBrand());
        updateProduct.setModel(updateProductRequest.getModel());
        updateProduct.setCategory(existingCategory.get());

        productService.createProduct(updateProduct);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(idProduct)
                .toUri();
        return ResponseEntity.created(uri).body(idProduct);
    }

    @GetMapping("/{idProduct}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> findProductById(@PathVariable @NotNull Long idProduct) {
        log.info("findProductById");

        return productService.findProductById(idProduct).map(product -> ResponseEntity.ok().body(product))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{idProduct}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long idProduct){
        log.trace("deleteProduct");

        Optional<Product> existingProduct = productService.findProductById(idProduct);

        if (existingProduct.isEmpty()) {
            log.trace("Product to deleted not exist");
            return ResponseEntity.status(409).body("Product not Exists.");
        }

        if (productService.removeProduct(idProduct)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(409).body("Could not be removed. There are offers associated with this client.");
        }
    }
}
