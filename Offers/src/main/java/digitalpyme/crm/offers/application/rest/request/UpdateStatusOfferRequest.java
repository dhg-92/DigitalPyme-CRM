package digitalpyme.crm.offers.application.rest.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UpdateStatusOfferRequest {

        @NotNull
        private String status;

}
