package intake.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import exception.NotFoundException;
import intake.model.Course;
import intake.model.Institution;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CatalogRepository {

    private List<Institution> institutions;

    @SneakyThrows
    public CatalogRepository(@Value("${catalog.path}") Resource catalogResource) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        this.institutions = objectMapper.readValue(catalogResource.getInputStream(), new TypeReference<List<Institution>>() {
        });
    }

    public List<Institution> institutions() {
        return institutions;
    }

    public Institution findInstitutionBySchacHome(String schacHome) {
        return institutions.stream()
                .filter(institution -> institution.getSchacHome().equals(schacHome))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }

    public Course findCourseByIdentifier(String identifier) {
        return institutions.stream()
                .map(Institution::getCourses)
                .flatMap(Collection::stream)
                .filter(course -> course.getIdentifier().equals(identifier))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }
}
