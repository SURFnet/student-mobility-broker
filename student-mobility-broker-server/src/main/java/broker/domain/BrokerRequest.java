package broker.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.Assert;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class BrokerRequest implements Serializable {

    private String homeInstitutionSchacHome;
    private String guestInstitutionSchacHome;
    private String offeringID;

    public void validate() {
        Assert.notNull(homeInstitutionSchacHome, "homeInstitutionSchacHome is required");
        Assert.notNull(guestInstitutionSchacHome, "guestInstitutionSchacHome is required");
        Assert.notNull(offeringID, "offeringID is required");
    }
}
