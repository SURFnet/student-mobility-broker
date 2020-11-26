package broker;

import broker.domain.Institution;
import broker.exception.NotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceRegistry {

    private List<Institution> institutions;

    @SneakyThrows
    public ServiceRegistry(@Value("${service_registry.path}") Resource serviceRegistryResource) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        this.institutions = objectMapper.readValue(serviceRegistryResource.getInputStream(), new TypeReference<List<Institution>>() {
        });
    }

    public Institution findInstitutionBySchacHome(String schacHome) {
        return institutions.stream()
                .filter(institution -> institution.getSchacHome().equals(schacHome))
                .findAny()
                .orElseThrow(NotFoundException::new);
    }
}
