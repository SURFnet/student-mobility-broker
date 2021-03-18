package broker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.net.URI;

@AllArgsConstructor
@Getter
public class EnrollmentRequest implements Serializable {

    private String personURI;
    private String resultsURI;
    private String scope;

}
