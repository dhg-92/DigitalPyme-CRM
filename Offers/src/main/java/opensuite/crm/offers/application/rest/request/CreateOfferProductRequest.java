package opensuite.crm.offers.application.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CreateOfferProductRequest {

        private Long productId;

        private BigDecimal productPrice;

        private BigDecimal tax;

        private Long quantity;
}


