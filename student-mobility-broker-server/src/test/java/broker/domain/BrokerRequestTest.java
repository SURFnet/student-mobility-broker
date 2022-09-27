package broker.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BrokerRequestTest {

    @Test
    void validate() {
        BrokerRequest brokerRequest = new BrokerRequest();
        brokerRequest.setGuestInstitutionSchacHome("guest");
        brokerRequest.setOfferingId("offering");
        brokerRequest.setHomeInstitutionSchacHome("home");
        brokerRequest.validate();
        assertEquals("course", brokerRequest.getType());

        brokerRequest.setType("program");
        brokerRequest.validate();
        assertEquals("program", brokerRequest.getType());
    }
}