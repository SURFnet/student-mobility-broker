package broker;

import broker.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ServiceRegistryTest {

    private ServiceRegistry subject = new ServiceRegistry(new ClassPathResource("service-registry.yml"));

    @Test
    void construct() {
        assertThrows(IOException.class, () -> new ServiceRegistry(new ClassPathResource("noop")));
    }

    @Test
    void findInstitutionBySchacHome() {
        assertThrows(NotFoundException.class, () -> subject.findInstitutionBySchacHome("nope"));
    }
}