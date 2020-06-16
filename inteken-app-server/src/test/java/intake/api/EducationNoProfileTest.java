package intake.api;

import exception.NotFoundException;
import intake.AbstractIntegrationTest;
import intake.model.Course;
import intake.model.Institution;
import intake.model.Registration;
import intake.repository.CatalogRepository;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

@ActiveProfiles(value = "prod", inheritProfiles = false)
public class EducationNoProfileTest extends AbstractIntegrationTest {

    @Test
    public void courses() {
        List<Institution> institutions = given().redirects().follow(false)
                .when()
                .contentType(ContentType.JSON)
                .get("/intake/api/public/institutions")
                .as(new TypeRef<List<Institution>>() {
                });
        assertEquals(3, institutions.size());

        List<Course> courses = institutions.stream()
                .map(Institution::getCourses)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        assertEquals(4, courses.size());
    }

    @Test
    public void institutionsSchacHomes() {
        List<String> institutions = given()
                .when()
                .contentType(ContentType.JSON)
                .get("/intake/api/public/institutions-schac-home")
                .as(new TypeRef<List<String>>() {
                });
        assertEquals(3, institutions.size());
    }

    @Test
    public void findInstitutionBySchacHome() {
        String schacHome = "groningen.org";
        Institution institution = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("schac_home", schacHome)
                .get("/intake/api/public/institution")
                .as(new TypeRef<Institution>() {
                });
        assertEquals(schacHome, institution.getSchacHome());
    }

    @Test
    public void findInstitutionBySchacHome404() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("schac_home", "nope")
                .get("/intake/api/public/institution")
                .then()
                .statusCode(404);
    }

    @Test
    public void findCourseByIdentifier() {
        String identifier = "198258DA-BBA3-47FA-95A1-3B29DD64B988";
        Course course = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("identifier", identifier)
                .get("/intake/api/public/course")
                .as(new TypeRef<Course>() {
                });
        assertEquals(identifier, course.getIdentifier());
    }

}