package broker.api;

import broker.domain.BrokerRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class BrokerController {

    private final String clientUrl;

    public BrokerController(@Value("${config.client_url}") String clientUrl) {
        this.clientUrl = clientUrl;
    }

    /*
     * Endpoint called by the student-mobility-broker form submit. Give browser-control back to the GUI
     */
    @PostMapping(value = "/api/broker", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public View brokerRequest(HttpServletRequest request, @ModelAttribute BrokerRequest brokerRequest) {
        brokerRequest.validate();
        return new RedirectView(clientUrl, clientUrl.startsWith("/"));
    }


}
