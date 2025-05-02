package opensuite.crm.offers.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long offerId;

    @NotNull
    private Long productId;

    @NotNull
    private BigDecimal productPrice;

    @NotNull
    private BigDecimal tax;

    @NotNull
    private Long quantity;
}
