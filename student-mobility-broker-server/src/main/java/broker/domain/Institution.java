package broker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.URI;

@Getter
@NoArgsConstructor
public class Institution implements Serializable {

    //Unique identifier of an Institution
    private String schacHome;
    //Not secured endpoint where course information can be retrieved
    private URI courseEndpoint;
    //Secured endpoint where person information can be retrieved about the authenticated user
    private URI personsEndpoint;
    //Secured endpoint where the actual registration will be done
    private URI registrationEndpoint;

}
