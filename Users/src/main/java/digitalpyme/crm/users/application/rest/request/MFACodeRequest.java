package digitalpyme.crm.users.application.rest.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MFACodeRequest {
    private String mfaCode;
}
