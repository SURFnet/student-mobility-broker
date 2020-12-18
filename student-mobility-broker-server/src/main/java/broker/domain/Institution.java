package broker.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.net.URI;

@Getter
@NoArgsConstructor
public class Institution implements Serializable {

    //Unique identifier of an Institution
    private String schacHome;

    //The human readable name of an Institution
    private String name;

    //The abbreviation of an Institution
    private String abbreviation;

    //Not secured endpoint where the authentication will start
    private URI authenticationEndpoint;

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

    //Authentication method for course endpoint
    private CourseAuthentication courseAuthentication;

    //Authentication userName for basic authentication course endpoint
    private String courseAuthenticationUserName;

    //Authentication password for basic authentication course endpoint
    private String courseAuthenticationPassword;

    //Authentication clientID for OAuth2 client credentials authentication course endpoint
    private String courseAuthenticationClientId;

    //Authentication secret for OAuth2 client credentials authentication course endpoint
    private String courseAuthenticationSecret;

    public void validate() {
        Assert.notNull(courseAuthentication, "courseAuthentication is required");
        if (courseAuthentication.equals(CourseAuthentication.BASIC)) {
            Assert.notNull(courseAuthenticationUserName, "courseAuthenticationUserName is required for BASIC authentication");
            Assert.notNull(courseAuthenticationPassword, "courseAuthenticationPassword is required for BASIC authentication");
        }
        if (courseAuthentication.equals(CourseAuthentication.OAUTH2)) {
            Assert.notNull(courseAuthenticationClientId, "courseAuthenticationClientId is required for BASIC authentication");
            Assert.notNull(courseAuthenticationSecret, "courseAuthenticationSecret is required for BASIC authentication");
        }

    }

    public Institution sanitize() {
        Institution institution = new Institution();
        institution.name = this.name;
        institution.logoURI = this.logoURI;
        institution.abbreviation = this.abbreviation;
        return institution;
    }

}
