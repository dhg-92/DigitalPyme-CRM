package opensuite.crm.offers;

import jakarta.transaction.Transactional;
import opensuite.crm.offers.domain.*;
import opensuite.crm.offers.domain.repository.*;
import opensuite.crm.offers.infrastructure.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({CategoryRepositoryImpl.class, ClientRepositoryImpl.class, OfferRepositoryImpl.class, ProductRepositoryImpl.class, OfferProductRepositoryImpl.class})
@Transactional
class OffersIntegrationTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OfferProductRepository offerProductRepository;

    @Test
    void integrationTest_Category() {

        assertFalse(categoryRepository.findAllCategories().isEmpty());

        Category categoryTestData = new Category();
        categoryTestData.setName("Category");
        categoryTestData.setDescription("Category Description");
        categoryTestData.setParentId(1L);

        Long id = categoryRepository.createCategory(categoryTestData);
        categoryTestData.setIdCategory(id);

        Optional<Category> category = categoryRepository.findCategoryById(id);

        assertThat(category).isNotEmpty();

        assertThat(category.get()).isNotNull();

        assertEquals(categoryTestData, category.get());

        assertFalse(categoryRepository.findCategoryByName(categoryTestData.getName()).isEmpty());
        assertFalse(categoryRepository.findCategoryByParentId(1L).isEmpty());
        assertTrue(categoryRepository.removeCategory(id));
        assertTrue(categoryRepository.findCategoryByName(categoryTestData.getName()).isEmpty());
    }

    @Test
    void integrationTest_Client() {

        assertFalse(clientRepository.findAllClients().isEmpty());

        Client clientTestData = new Client();
        clientTestData.setIdClient(1L);
        clientTestData.setName("Name");
        clientTestData.setSurname("Surname");
        clientTestData.setEmail("Email");
        clientTestData.setCompany("Company");
        clientTestData.setPhone("123456789");
        clientTestData.setAddress("Address");
        clientTestData.setCity("City");
        clientTestData.setZipCode(12345L);
        clientTestData.setCountry("Country");

        Long id = clientRepository.createClient(clientTestData);
        clientTestData.setIdClient(id);

        Optional<Client> client = clientRepository.findClientById(id);

        assertThat(client).isNotEmpty();

        assertThat(client.get()).isNotNull();

        assertEquals(clientTestData, client.get());

        assertFalse(clientRepository.findClientByEmail(clientTestData.getEmail()).isEmpty());
        assertTrue(clientRepository.removeClient(id));
    }

    @Test
    void integrationTest_Offer() {

        assertFalse(offerRepository.findAllOffer().isEmpty());

        Offer clientTestData = new Offer();
        clientTestData.setName("Name");
        clientTestData.setDate(new Date());
        clientTestData.setClient(clientRepository.findClientById(1L).get());
        clientTestData.setStatus("Status");
        clientTestData.setTotalPrice(new BigDecimal("100.00"));


        Long id = offerRepository.createOffer(clientTestData);
        clientTestData.setIdOffer(id);

        Optional<Offer> offer = offerRepository.findOfferByIdOffer(id);

        assertThat(offer).isNotEmpty();

        assertThat(offer.get()).isNotNull();

        assertEquals(clientTestData, offer.get());

        assertTrue(offerRepository.removeOffer(clientTestData));
        assertTrue(offerRepository.findOfferByIdOffer(id).isEmpty());
    }

    @Test
    void integrationTest_Product() {

        assertFalse(offerRepository.findAllOffer().isEmpty());

        Product productTestData = new Product();
        productTestData.setName("Name");
        productTestData.setDescription("Description");
        productTestData.setDefaultPrice(new BigDecimal("100.00"));
        productTestData.setDefaultTax(new BigDecimal("7.00"));
        productTestData.setBrand("Marca");
        productTestData.setModel("Model");
        productTestData.setCategory(categoryRepository.findCategoryById(1L).get());

        Long id = productRepository.createProduct(productTestData);
        productTestData.setIdProduct(id);

        Optional<Product> product = productRepository.findProductById(id);

        assertThat(product).isNotEmpty();

        assertThat(product.get()).isNotNull();

        assertEquals(productTestData, product.get());

        assertTrue(productRepository.removeProduct(productTestData.getIdProduct()));
        assertTrue(productRepository.findProductById(id).isEmpty());
    }

    @Test
    void integrationTest_OfferProduct() {
        assertFalse(offerProductRepository.findOfferProductById(1L).isEmpty());

        OfferProduct offerProductTestData = new OfferProduct();
        offerProductTestData.setBaseproductPrice(new BigDecimal("100.00"));
        offerProductTestData.setProductPrice(new BigDecimal("130.00"));
        offerProductTestData.setTax(new BigDecimal("21.00"));
        offerProductTestData.setQuantity(1L);
        offerProductTestData.setMargin(new BigDecimal("10.00"));
        offerProductTestData.setOfferId(1L);
        offerProductTestData.setProductId(1L);

        ClientEntity clientEntity = ClientEntity.fromDomain(clientRepository.findClientById(1L).get());
        OfferEntity offerEntity = OfferEntity.fromDomain(offerRepository.findOfferByIdOffer(offerProductTestData.getOfferId()).get(), clientEntity);
        CategoryEntity categoryEntity = CategoryEntity.fromDomain(categoryRepository.findCategoryById(1L).get());
        ProductEntity productEntity = ProductEntity.fromDomain(productRepository.findProductById(offerProductTestData.getProductId()).get(), categoryEntity);

        assertTrue(offerProductRepository.removeOfferProductForIdOffer(offerProductTestData.getOfferId()));

        assertTrue(offerProductRepository.findOfferProductByOfferId(offerProductTestData.getOfferId()).isEmpty());

        assertTrue(offerProductRepository.createAllOfferProduct(List.of(OfferProductEntity.fromDomain(offerProductTestData, offerEntity, productEntity))));

        List<OfferProduct> offerProduct = offerProductRepository.findOfferProductByOfferId(1L);

        assertThat(offerProduct).isNotEmpty();

        assertThat(offerProduct.get(0)).isNotNull();

        assertEquals(1L, offerProduct.size());
    }
}
