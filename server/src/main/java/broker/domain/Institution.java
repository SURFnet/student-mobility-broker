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

    //Public endpoint which describes the privacy policy
    private URI privacyEndpoint;

    //Secured endpoint where person information can be retrieved about the authenticated user
    private String personsEndpoint;

    //Authentication method for person endpoint
    private PersonAuthentication personAuthentication;

    //Secured endpoint where the association for a person can be posted to the home institution
    private String associationsEndpoint;

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

    //Do we get offerings from eduhub or from the courseEndpoint
    private boolean useEduHubForOffering;

    //Do we send students to the waiting queue-it or do we allow immediate registration
    private boolean useQueueIt;

    //The name of the waiting room if queueIt is enabled
    private String queueItWaitingRoom;

    //The secret for token validation
    private String queueItSecret;

    public void validate() {
        Assert.notNull(courseAuthentication, "courseAuthentication is required");
        if (courseAuthentication.equals(CourseAuthentication.BASIC)) {
            Assert.notNull(courseAuthenticationUserName, "courseAuthenticationUserName is required for BASIC authentication");
            Assert.notNull(courseAuthenticationPassword, "courseAuthenticationPassword is required for BASIC authentication");
        }
        Assert.notNull(name, "name is required");
        Assert.notNull(abbreviation, "abbreviation is required");
        Assert.notNull(courseEndpoint, "courseEndpoint is required");
        Assert.notNull(personsEndpoint, "personsEndpoint is required");
        Assert.notNull(personAuthentication, "personAuthentication is required");
        Assert.notNull(associationsEndpoint, "associationsEndpoint is required");
        Assert.notNull(authenticationEndpoint, "authenticationEndpoint is required");
        Assert.notNull(registrationEndpoint, "registrationEndpoint is required");
        Assert.notNull(registrationUser, "registrationUser is required");
        Assert.notNull(registrationPassword, "registrationPassword is required");
        Assert.notNull(logoURI, "logoURI is required");
        Assert.notNull(scopes, "scopes is required");
        Assert.notNull(privacyEndpoint, "privacyEndpoint is required");
        if (useQueueIt) {
            Assert.hasLength(queueItWaitingRoom, "If queueIt is enabled, then specify the queueItWaitingRoom");
            Assert.isTrue(queueItWaitingRoom.matches("[a-zA-Z0-9]+"), "queueItWaitingRoom may only contain letters");
            Assert.hasLength(queueItSecret, "If queueIt is enabled, then specify the queueItWaitingRoom");
        }
    }

    public Institution(boolean useQueueIt, String queueItWaitingRoom, String queueItSecret) {
        this.useQueueIt = useQueueIt;
        this.queueItWaitingRoom = queueItWaitingRoom;
        this.queueItSecret = queueItSecret;
    }

    public Institution sanitize() {
        Institution institution = new Institution();
        institution.schacHome = this.schacHome;
        institution.name = this.name;
        institution.logoURI = this.logoURI;
        institution.privacyEndpoint = this.privacyEndpoint;
        institution.abbreviation = this.abbreviation;
        institution.courseEndpoint = this.courseEndpoint;
        institution.courseAuthentication = this.courseAuthentication;
        institution.scopes = this.scopes;
        return institution;
    }

}
