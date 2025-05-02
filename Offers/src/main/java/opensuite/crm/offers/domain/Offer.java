package opensuite.crm.offers.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOffer;

    @NotNull
    private String name;

    @NotNull
    private Date date;

    @NotNull
    private Client client;

    @NotNull
    private String status;

    @NotNull
    private BigDecimal totalPrice;

}
