package broker.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BrokerRequest implements Serializable {

    private String homeInstitutionSchacHome;
    private String guestInstitutionSchacHome;
    private String offeringId;
    private String type;

    public void validate() {
        Assert.notNull(homeInstitutionSchacHome, "homeInstitutionSchacHome is required");
        Assert.notNull(guestInstitutionSchacHome, "guestInstitutionSchacHome is required");
        Assert.notNull(offeringId, "offeringId is required");
        if (!StringUtils.hasText(type)) {
            type = "course";
        }
    }
}
