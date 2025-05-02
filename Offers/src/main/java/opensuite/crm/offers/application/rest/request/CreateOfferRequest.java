package opensuite.crm.offers.application.rest.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CreateOfferRequest {

        @NotNull
        private String name;

        @NotNull
        private Date date;

        @NotNull
        private Long clientId;

        @NotNull
        private String status;

        private BigDecimal totalPrice;
}
