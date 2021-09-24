package broker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class EnrollmentRequest implements Serializable {

    private String personURI;
    private PersonAuthentication personAuth;
    private String resultsURI;
    private String scope;

}
