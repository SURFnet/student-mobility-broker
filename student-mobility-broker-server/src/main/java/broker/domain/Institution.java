package broker.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.net.URI;

@Getter
@NoArgsConstructor
public class Institution implements Serializable {

    //Unique identifier of an Institution
    private String schacHome;
    //The human readable name of an Institution

    private String name;
    //Not secured endpoint where course information can be retrieved

    private URI courseEndpoint;
    //Secured endpoint where person information can be retrieved about the authenticated user

    private String personsEndpoint;
    //Secured endpoint where the actual registration will be done

    private URI registrationEndpoint;
    //The username for basic authentication at the registrationEndpoint

    private String registrationUser;

    //The password for basic authentication at the registrationEndpoint
    private String registrationPassword;

    //URI of the logo
    private URI logoURI;

    //Space separated scopes
    private String scopes;

    public Institution sanitize() {
        Institution institution = new Institution();
        institution.name = this.name;
        institution.logoURI = this.logoURI;
        return institution;
    }

}
