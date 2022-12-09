package broker.registry;

import broker.domain.Institution;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InMemoryInstitutionRegistry implements InstitutionRegistry {

    private final Map<String, Institution> institutions;

    @SneakyThrows
    public InMemoryInstitutionRegistry(@Value("${service_registry.path}") Resource serviceRegistryResource) {
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        List<Institution> institutionList = objectMapper.readValue(serviceRegistryResource.getInputStream(), new TypeReference<List<Institution>>() {
        });
        institutionList.forEach(Institution::validate);
        this.institutions = institutionList.stream().collect(Collectors.toMap(Institution::getSchacHome, Function.identity()));
    }

    @Override
    public Optional<Institution> findInstitutionBySchacHome(String schacHome) {
        return Optional.ofNullable(institutions.get(schacHome));
    }

    @Override
    public Collection<Institution> allInstitutions() {
        return institutions.values();
    }
}
