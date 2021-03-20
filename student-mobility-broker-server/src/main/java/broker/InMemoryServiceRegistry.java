package broker;

import broker.domain.Institution;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InMemoryServiceRegistry implements ServiceRegistry {

    private final List<Institution> institutions;

    @SneakyThrows
    public InMemoryServiceRegistry(@Value("${service_registry.path}") Resource serviceRegistryResource) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        this.institutions = objectMapper.readValue(serviceRegistryResource.getInputStream(), new TypeReference<List<Institution>>() {
        });
        this.institutions.forEach(Institution::validate);
    }

    @Override
    public Optional<Institution> findInstitutionBySchacHome(String schacHome) {
        return institutions.stream()
                .filter(institution -> institution.getSchacHome().equals(schacHome))
                .findAny();
    }

    @Override
    public List<Institution> allInstitutions() {
        return institutions;
    }
}
