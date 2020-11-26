package broker.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import java.net.URI;

@NoArgsConstructor
@Getter
@Setter
public class BrokerRequest {

    private String homeInstitutionSchacHome;
    private String guestInstitutionSchacHome;
    private URI offeringOOAPIUri;

    public void validate() {
        Assert.notNull(homeInstitutionSchacHome, "homeInstitutionSchacHome is required");
        Assert.notNull(guestInstitutionSchacHome, "guestInstitutionSchacHome is required");
        Assert.notNull(offeringOOAPIUri, "offeringOOAPIUri is required");
    }
}
