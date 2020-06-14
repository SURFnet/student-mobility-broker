package intake.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Course {

    private String identifier;
    private String name;
    private String description;
    private List<String> requiredScopes;

}
