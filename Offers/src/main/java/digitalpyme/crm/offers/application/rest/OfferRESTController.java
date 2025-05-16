package digitalpyme.crm.offers.application.rest;

import com.itextpdf.text.DocumentException;
import digitalpyme.crm.offers.application.rest.request.CreateOfferProductRequest;
import digitalpyme.crm.offers.application.rest.request.CreateOfferRequest;
import digitalpyme.crm.offers.application.rest.request.UpdateStatusOfferRequest;
import digitalpyme.crm.offers.domain.services.*;
import digitalpyme.crm.offers.infrastructure.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import digitalpyme.crm.offers.application.rest.data.SendMailData;
import digitalpyme.crm.offers.application.rest.request.*;
import digitalpyme.crm.offers.application.rest.response.OfferPrice;
import digitalpyme.crm.offers.application.rest.response.OfferExtendResponse;
import digitalpyme.crm.offers.application.rest.response.OfferProductsExtendResponse;
import digitalpyme.crm.offers.application.rest.response.OfferResponse;
import digitalpyme.crm.offers.domain.Client;
import digitalpyme.crm.offers.domain.Offer;
import digitalpyme.crm.offers.domain.OfferProduct;
import digitalpyme.crm.offers.domain.Product;
import digitalpyme.crm.offers.domain.services.*;
import digitalpyme.crm.offers.infrastructure.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/offers")
public class OfferRESTController {

    private final OfferService offerService;
    private final OfferProductService offerProductService;
    private final ProductService productService;
    private final ClientService clientService;
    private final PrintToPDFServices printToPDFServices;
    private final KafkaTemplate<String, SendMailData> offerKafkaTemplate;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OfferResponse> getAllOffers() {
        log.trace("getAllOffers");

        return offerService.findAllOffers().stream()
                .map(OfferResponse::offerToGetOfferResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{idOffer}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> findOfferById(@PathVariable @NotNull Long idOffer) {
        log.info("findOffer");

        return offerService.findOfferByIdOffer(idOffer).map(offer -> ResponseEntity.ok().body(offer))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{idOffer}/extendInfo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OfferExtendResponse> extendInfoOffer(@PathVariable @NotNull Long idOffer) {
        log.info("extendInfoOffer");

        Offer offerData = offerService.findOfferByIdOffer(idOffer).get();
        OfferExtendResponse returnResponse = new OfferExtendResponse(offerData);
        returnResponse.setClient(offerData.getClient());
        List<OfferProduct> items = offerProductService.findOfferProductByOfferId(idOffer);
        returnResponse.setTotalItems(items.size());

        List<OfferProductsExtendResponse> itemsToResponse = new ArrayList<>();

        BigDecimal totalBeforeTAX = BigDecimal.ZERO;
        BigDecimal totalWithTAX = BigDecimal.ZERO;

        for (int i = 0; i < items.size(); i++) {
            Product productInfo = productService.findProductById(items.get(i).getProductId()).get();

            BigDecimal baseproductPrice = items.get(i).getBaseproductPrice();
            BigDecimal quantity = BigDecimal.valueOf(items.get(i).getQuantity());

            BigDecimal totalPriceProduct = (baseproductPrice.add(baseproductPrice.multiply(items.get(i).getMargin().divide(BigDecimal.valueOf(100))))).multiply(quantity);

            totalBeforeTAX = totalBeforeTAX.add(totalPriceProduct);
            totalWithTAX = totalWithTAX.add(totalPriceProduct.multiply(items.get(i).getTax().divide(BigDecimal.valueOf(100))));

            itemsToResponse.add(new OfferProductsExtendResponse(productInfo, items.get(i)));
        }

        OfferPrice prices = new OfferPrice();
        prices.setTotalBeforeTAX(totalBeforeTAX);
        prices.setTotalWithTAX(totalWithTAX);
        prices.setTotalOffer(totalBeforeTAX.add(totalWithTAX));

        returnResponse.setPrices(prices);
        returnResponse.setItems(itemsToResponse);

        return ResponseEntity.ok().body(returnResponse);
    }

    @GetMapping("/{idOffer}/pdf")
    public ResponseEntity<?> printOfferToPDF(@PathVariable Long idOffer) {
        log.info("printOfferToPDF");

        Offer offerData = offerService.findOfferByIdOffer(idOffer).get();
        if (offerData.getIdOffer() == null) {
            log.trace("Offer can't found");
            return ResponseEntity.status(409).body("Offer can't found.");
        }

        OfferExtendResponse returnResponse = new OfferExtendResponse(offerData);
        returnResponse.setClient(offerData.getClient());

        List<OfferProduct> items = offerProductService.findOfferProductByOfferId(idOffer);
        if (items.isEmpty()){
            log.trace("Offer can't print, not have items");
            return ResponseEntity.status(409).body("Offer can't print, not have items.");
        }

        returnResponse.setTotalItems(items.size());

        List<OfferProductsExtendResponse> itemsToResponse = new ArrayList<>();

        BigDecimal totalBeforeTAX = BigDecimal.ZERO;
        BigDecimal totalWithTAX = BigDecimal.ZERO;

        for (int i = 0; i < items.size(); i++) {
            Product productInfo = productService.findProductById(items.get(i).getProductId()).get();

            BigDecimal totalPriceProduct = items.get(i).getProductPrice().multiply(BigDecimal.valueOf(items.get(i).getQuantity()));

            totalBeforeTAX = totalBeforeTAX.add(totalPriceProduct);
            totalWithTAX = totalWithTAX.add(totalPriceProduct.multiply(items.get(i).getTax().divide(BigDecimal.valueOf(100))));

            itemsToResponse.add(new OfferProductsExtendResponse(productInfo, items.get(i)));
        }

        OfferPrice prices = new OfferPrice();
        prices.setTotalBeforeTAX(totalBeforeTAX);
        prices.setTotalWithTAX(totalWithTAX);
        prices.setTotalOffer(totalBeforeTAX.add(totalWithTAX));

        returnResponse.setPrices(prices);
        returnResponse.setItems(itemsToResponse);

        String fileName = "inline; filename=Offer-"+returnResponse.getIdOffer()+".pdf";
        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", fileName)
                .body(printToPDFServices.generatePdf(returnResponse));
        }

    @PostMapping("/{idOffer}/sendoffer")
    public ResponseEntity<?> sendOffer(@PathVariable Long idOffer) throws DocumentException {
        log.info("sendOffer");
        Optional<Offer> existingOffer = offerService.findOfferByIdOffer(idOffer);

        if (existingOffer.isEmpty()) {
            log.trace("Offer to send not exist");
            return ResponseEntity.status(409).body("Offer to send not Exists.");
        }

        SendMailData sendData = new SendMailData(UUID.randomUUID().toString(), idOffer, existingOffer.get().getClient().getEmail());
        offerKafkaTemplate.send("sendOffer", sendData);

        existingOffer.get().setStatus("Enviada");
        offerService.updateOffer(existingOffer.get());

        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createOffer(@RequestBody @Valid CreateOfferRequest createOfferRequest) {
        log.trace("createOffer");

        log.trace("Creating offer " + createOfferRequest);

        Optional<Client> existingClient = clientService.findClientById(createOfferRequest.getClientId());

        if (existingClient.isEmpty()) {
            log.trace("Client not exist " + existingClient);
            return ResponseEntity.status(409).body("Client not Exists.");
        }

        Offer newOffer = new Offer();
        newOffer.setName(createOfferRequest.getName());
        newOffer.setDate(createOfferRequest.getDate());
        newOffer.setClient(existingClient.get());
        newOffer.setStatus(createOfferRequest.getStatus());
        if(createOfferRequest.getTotalPrice() == null) {
            newOffer.setTotalPrice(new BigDecimal("0.00"));
        } else {
            newOffer.setTotalPrice(createOfferRequest.getTotalPrice());
        }

        Long offerId = offerService.createOffer(newOffer);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(offerId)
                .toUri();
        return ResponseEntity.created(uri).body(offerId);
    }

    @PutMapping("/{idOffer}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateOffer(@RequestBody @NotNull @Valid CreateOfferRequest offerRequest, @PathVariable Long idOffer) {
        log.trace("UpdateOffer");

        Optional<Client> existingClient = clientService.findClientById(offerRequest.getClientId());

        if (existingClient.isEmpty()) {
            log.trace("Client not exist " + existingClient);
            return ResponseEntity.status(409).body("Client not Exists.");
        }

        log.trace("Update offer " + offerRequest);

        Optional<Offer> offerFound = offerService.findOfferByIdOffer(idOffer);

        if (offerFound.get().getStatus().equals("Aceptada") || offerFound.get().getStatus().equals("Enviada") || offerFound.get().getStatus().equals("Rechazada")){
            log.trace("The accepted or sent offer cannot be modified " + existingClient);
            return ResponseEntity.status(409).body("The accepted or sent offer cannot be modified.");
        }

        if (offerFound.isPresent()) {
            Offer udateOffer = new Offer();
            udateOffer.setIdOffer(idOffer);
            udateOffer.setName(offerRequest.getName());
            udateOffer.setDate(offerRequest.getDate());
            udateOffer.setClient(existingClient.get());
            udateOffer.setStatus(offerRequest.getStatus());
            if(offerRequest.getTotalPrice() == null) {
                udateOffer.setTotalPrice(offerFound.get().getTotalPrice());
            } else {
                udateOffer.setTotalPrice(offerRequest.getTotalPrice());
            }

            Long offerId = offerService.updateOffer(udateOffer);
            return ResponseEntity.ok(offerId);
        } else {
            log.trace("Offer not exist " + offerRequest);
            return ResponseEntity.status(404).body("Offer not Exists.");
        }
    }

    @PutMapping("/{idOffer}/status")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> updateStatusOffer(@RequestBody @NotNull @Valid UpdateStatusOfferRequest offerStatusRequest, @PathVariable Long idOffer) {
        log.trace("updateStatusOffer");

        log.trace("Update status offer " + offerStatusRequest);

        Optional<Offer> offerFound = offerService.findOfferByIdOffer(idOffer);

        if (!offerFound.isPresent()) {
            log.trace("Offer not exist " + idOffer);
            return ResponseEntity.status(404).body("Offer not Exists.");
        }

        if (offerStatusRequest.getStatus().equals("Aceptada") || offerStatusRequest.getStatus().equals("Rechazada")){
            offerFound.get().setStatus(offerStatusRequest.getStatus());
            Long offerId = offerService.updateOffer(offerFound.get());
            return ResponseEntity.ok(offerId);
        } else {
            log.trace("Unable to update status in the offer " + idOffer);
            return ResponseEntity.status(409).body("Unable to update status.");
        }
    }

    @DeleteMapping("/{idOffer}")
    public ResponseEntity<?> deleteOffer(@PathVariable Long idOffer){
        log.trace("deleteOffer");

        Optional<Offer> existingOffer = offerService.findOfferByIdOffer(idOffer);

        if (existingOffer.isEmpty()) {
            log.trace("Offer to deleted not exist");
            return ResponseEntity.status(409).body("Offer not Exists.");
        }

        if (offerService.removeOffer(existingOffer.get())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(409).body("Could not be removed. There are offers associated with this client.");
        }
    }

    @PostMapping("/{idOffer}/products")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> createOfferProductById(@PathVariable @NotNull Long idOffer, @RequestBody @Valid List<CreateOfferProductRequest> offerProductData) {
        log.info("createOfferProductById");

        Optional<Offer> offerData = offerService.findOfferByIdOffer(idOffer);
        if (offerData.isEmpty()) {
            log.trace("Offer to update not exist");
            return ResponseEntity.status(409).body("Offer not Exists.");
        }

        if (!offerProductService.removeOfferProductForIdOffer(idOffer)) {
            log.trace("Error deleting the products associated with the offer");
            return ResponseEntity.status(409).body("Error deleting products in offer.");
        }

        OfferEntity offerEntity = OfferEntity.fromDomain(offerData.get(), ClientEntity.fromDomain(offerData.get().getClient()));

        List<OfferProductEntity> offerProductEntity = new ArrayList<>();
        for (int i = 0; i < offerProductData.size(); i++) {

            Optional<Product> productData = productService.findProductById(offerProductData.get(i).getProductId());
            if (productData.isEmpty()) {
                log.trace("Product to update not exist");
                return ResponseEntity.status(409).body("Product not Exists.");
            }
            ProductEntity productEntity = ProductEntity.fromDomain(productData.get(), CategoryEntity.fromDomain(productData.get().getCategory()));

            OfferProduct offerProduct = new OfferProduct();
            offerProduct.setBaseproductPrice(offerProductData.get(i).getBaseproductPrice());
            offerProduct.setProductPrice(offerProductData.get(i).getProductPrice());
            offerProduct.setTax(offerProductData.get(i).getTax());
            offerProduct.setQuantity(offerProductData.get(i).getQuantity());
            offerProduct.setMargin(offerProductData.get(i).getMargin());

            offerProductEntity.add(OfferProductEntity.fromDomain(offerProduct, offerEntity, productEntity));
        }

        offerProductService.createAllOfferProduct(offerProductEntity);
        return ResponseEntity.ok().body("");
    }


}
