package digitalpyme.crm.offers;

import digitalpyme.crm.offers.domain.Category;
import digitalpyme.crm.offers.domain.Client;
import digitalpyme.crm.offers.domain.Offer;
import digitalpyme.crm.offers.domain.Product;
import digitalpyme.crm.offers.domain.repository.*;
import digitalpyme.crm.offers.domain.services.*;
import digitalpyme.crm.offers.infrastructure.repository.OfferEntity;
import digitalpyme.crm.offers.infrastructure.repository.OfferProductEntity;
import digitalpyme.crm.offers.infrastructure.repository.ProductEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OffersUnitTest {

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private OfferProductServiceImpl offerProductService;

    @Mock
    private OfferProductRepository offerProductRepository;

    @InjectMocks
    private OfferServiceImpl offerService;

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void unitTest_Categories() {
        assertTrue(categoryService.findAllCategories().isEmpty());
        assertTrue(categoryService.findCategoryById(1L).isEmpty());
        assertFalse(categoryService.removeCategory(1L));
        assertTrue(categoryService.findCategoryByParentId(1L).isEmpty());

        Category category = new Category();
        category.setIdCategory(1L);
        category.setName("Category");
        category.setDescription("Category Description");
        category.setParentId(null);

        when(categoryRepository.createCategory(category)).thenReturn(category.getIdCategory());
        assertEquals(1L, categoryService.createCategory(category));

        assertTrue(categoryService.findCategoryById(1L).isEmpty());
        when(categoryRepository.findCategoryById(category.getIdCategory())).thenReturn(Optional.of(category));
        assertFalse(categoryService.findCategoryById(1L).isEmpty());

        assertTrue(categoryService.findCategoryByName("Category").isEmpty());
        when(categoryRepository.findCategoryByName(category.getName())).thenReturn(Optional.of(category));
        assertFalse(categoryService.findCategoryByName("Category").isEmpty());

        when(categoryRepository.removeCategory(1L)).thenReturn(true);
        assertTrue(categoryService.removeCategory(1L));
    }

    @Test
    void unitTest_Clients() {
        assertTrue(clientService.findClientById(1L).isEmpty());
        assertTrue(clientService.findAllClients().isEmpty());

        Client client = new Client();
        client.setIdClient(1L);
        client.setName("Name");
        client.setSurname("Surname");
        client.setEmail("Email");
        client.setCompany("Company");
        client.setPhone("123456789");
        client.setAddress("Address");
        client.setCity("City");
        client.setZipCode(12345L);
        client.setCountry("Country");

        when(clientRepository.createClient(client)).thenReturn(client.getIdClient());
        assertEquals(1L, clientService.createClient(client));

        assertTrue(clientService.findClientById(1L).isEmpty());
        when(clientRepository.findClientById(client.getIdClient())).thenReturn(Optional.of(client));
        assertFalse(clientService.findClientById(1L).isEmpty());

        assertTrue(clientService.findClientByEmail("mail-not-exist@mail.com").isEmpty());
        when(clientRepository.findClientByEmail(client.getEmail())).thenReturn(Optional.of(client));
        assertFalse(clientService.findClientByEmail(client.getEmail()).isEmpty());

    }

    @Test
    void unitTest_OffersProducts() {
        assertTrue(offerProductService.findOfferProductById(1L).isEmpty());
        assertTrue(offerProductService.findOfferProductByOfferId(1L).isEmpty());

        ProductEntity productEntity = new ProductEntity();
        productEntity.setIdProduct(1L);
        OfferEntity offerEntity = new OfferEntity();
        offerEntity.setIdOffer(1L);

        OfferProductEntity offerProductEntity = new OfferProductEntity();
        offerProductEntity.setId(1L);
        offerProductEntity.setOfferId(offerEntity);
        offerProductEntity.setProductId(productEntity);
        offerProductEntity.setBaseproductPrice(BigDecimal.valueOf(100));
        offerProductEntity.setQuantity(1L);
        offerProductEntity.setTax(BigDecimal.valueOf(10));
        offerProductEntity.setMargin(BigDecimal.valueOf(10));
        offerProductEntity.setProductPrice(BigDecimal.valueOf(110));

        when(offerProductRepository.createAllOfferProduct(List.of(offerProductEntity))).thenReturn(true);
        assertTrue(offerProductService.createAllOfferProduct(List.of(offerProductEntity)));

        assertTrue(offerProductService.findOfferProductById(1L).isEmpty());
        when(offerProductRepository.findOfferProductById(1L)).thenReturn(Optional.of(offerProductEntity.toDomain()));
        assertFalse(offerProductService.findOfferProductById(1L).isEmpty());

        assertTrue(offerProductService.findOfferProductByOfferId(1L).isEmpty());
        when(offerProductRepository.findOfferProductByOfferId(1L)).thenReturn(List.of(offerProductEntity.toDomain()));
        assertFalse(offerProductService.findOfferProductByOfferId(1L).isEmpty());
    }

    @Test
    void unitTest_Offers() {
        assertTrue(offerService.findOfferByIdOffer(1L).isEmpty());
        assertTrue(offerService.findAllOffers().isEmpty());

        Offer offer = new Offer();
        offer.setIdOffer(1L);
        offer.setName("name");
        offer.setTotalPrice(BigDecimal.valueOf(100.00));
        offer.setStatus("Pending");
        offer.setDate(new Date());
        Client client = new Client();
        client.setIdClient(1L);
        offer.setClient(client);

        when(offerRepository.createOffer(offer)).thenReturn(1L);
        assertEquals(offer.getIdOffer(), offerService.createOffer(offer));

        offer.setStatus("Accepted");

        when(offerRepository.updateOffer(offer)).thenReturn(1L);
        assertEquals(offer.getIdOffer(), offerService.updateOffer(offer));
    }

    @Test
    void unitTest_Products() {
        assertTrue(productService.findProductById(1L).isEmpty());
        assertTrue(productService.findAllProducts().isEmpty());

        Product product = new Product();
        product.setIdProduct(1L);
        product.setName("Name");
        product.setDescription("Description");
        product.setDefaultPrice(BigDecimal.valueOf(100));
        product.setDefaultTax(BigDecimal.valueOf(10));
        product.setBrand("Marca");
        product.setModel("Modelo");
        Category category = new Category();
        category.setIdCategory(1L);
        product.setCategory(category);

        when(productRepository.createProduct(product)).thenReturn(1L);
        assertEquals(product.getIdProduct(), productService.createProduct(product));
    }
}