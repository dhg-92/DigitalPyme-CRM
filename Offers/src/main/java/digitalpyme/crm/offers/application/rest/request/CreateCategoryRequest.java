package digitalpyme.crm.offers.application.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CreateCategoryRequest {

        private String name;

        private String description;

        private Long parentId;

}

