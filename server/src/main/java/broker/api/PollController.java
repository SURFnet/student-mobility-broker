package broker.api;

import broker.domain.PollRequest;
import broker.mail.MailBox;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class PollController {

    private static final Log LOG = LogFactory.getLog(PollController.class);

    private final MailBox mailBox;

    public PollController(MailBox mailBox) {
        this.mailBox = mailBox;
    }

    /*
     * Endpoint called by the playground GUI to get all institutions
     */
    @PostMapping(value = "/api/poll")
    public ResponseEntity<Map<String, Integer>> pollRequest(@RequestBody PollRequest pollRequest) {
        LOG.debug("Received poll submit: " + pollRequest);

        mailBox.sendPollMail(pollRequest);
        Map<String, Integer> res = new HashMap<>();
        res.put("status", HttpStatus.OK.value());
        return ResponseEntity.ok(res);
    }

}
