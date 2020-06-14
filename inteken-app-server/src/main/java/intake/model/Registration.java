package intake.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Registration {

    private String courseIdentifier;
    private String schacHomeInstitution;
    private String schacHomeGuestInstitution;
    private boolean preview;
}
