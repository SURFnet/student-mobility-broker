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
@Setter
public class EnrollmentRequest implements Serializable {

    private URI offering;
    private URI person;
    private String scope;
    private URI returnTo;

}
