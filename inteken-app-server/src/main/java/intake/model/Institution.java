package intake.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Institution {

    private String name;
    private String schacHome;
    private String apiBaseUrl;
    private List<Course> courses;

}
