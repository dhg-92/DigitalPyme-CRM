package opensuite.crm.offers.application.rest.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
@Setter
@ToString
public class OfferPrice {
    private BigDecimal totalBeforeTAX;

    public void setTotalBeforeTAX(BigDecimal totalBeforeTAX) {
        this.totalBeforeTAX = totalBeforeTAX.setScale(2, RoundingMode.HALF_UP);;
    }

    public void setTotalWithTAX(BigDecimal totalWithTAX) {
        this.totalWithTAX = totalWithTAX.setScale(2, RoundingMode.HALF_UP);;
    }

    public void setTotalOffer(BigDecimal totalOffer) {
        this.totalOffer = totalOffer.setScale(2, RoundingMode.HALF_UP);;
    }

    private BigDecimal totalWithTAX;
    private BigDecimal totalOffer;
    public void OfferPrice() {
        this.totalBeforeTAX = BigDecimal.ZERO;
        this.totalWithTAX = BigDecimal.ZERO;
        this.totalOffer = BigDecimal.ZERO;
    }

}
