package broker;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServiceRegistryTest {

    private ServiceRegistry subject = new InMemoryServiceRegistry(new ClassPathResource("service-registry.yml"));

    @Test
    void construct() {
        assertThrows(IOException.class, () -> new InMemoryServiceRegistry(new ClassPathResource("noop")));
    }

    @Test
    void findInstitutionBySchacHome() {
        assertFalse(subject.findInstitutionBySchacHome("nope").isPresent());
    }
}