package intake.repository;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.junit.Assert.*;

public class CatalogRepositoryTest {

    @Test(expected = IOException.class)
    public void institutions() {
        new CatalogRepository(new ClassPathResource("/nope"));
    }
}