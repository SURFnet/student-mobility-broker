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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class EducationTest extends AbstractIntegrationTest {

    @Autowired
    private CatalogRepository catalogRepository;

    @Test
    public void courses() {
        List<Institution> institutions = given().redirects().follow(false)
                .when()
                .contentType(ContentType.JSON)
                .get("/intake/api/institutions")
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
    public void t404() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .get("/intake/api/404")
                .then()
                .statusCode(404);
    }

    @Test
    public void registerPreview() {
        String expected = "http://localhost:8091/uva?course=57761270-2DFC-498E-88DC-CF8DB5FC5B1A&scope=student_information&apiUrl=http://localhost:8091/ou";
        this.doRegistration("ou.org", "uva.org", "History", true)
                .body("url", equalTo(expected));
    }

    @Test
    public void registerRedirect() {
        String expected = "http://localhost:8091/uva?course=57761270-2DFC-498E-88DC-CF8DB5FC5B1A&scope=student_information&apiUrl=http://localhost:8091/ou";
        this.doRegistration("ou.org", "uva.org", "History", false)
                .statusCode(302)
                .header("Location", equalTo(expected));
    }

    @Test
    public void registerNoAffiliation() {
        this.doRegistration("groningen.org", "uva.org", "Psychology", false)
                .statusCode(409);
    }

    private ValidatableResponse doRegistration(String schacHome,
                                               String schacHomeGuest,
                                               String courseName,
                                               boolean preview) {
        String courseIdentifier = catalogRepository.findInstitutionBySchacHome(schacHome)
                .getCourses()
                .stream()
                .filter(course -> course.getName().equalsIgnoreCase(courseName))
                .findAny().orElseThrow(NotFoundException::new).getIdentifier();

        Registration registration = new Registration(courseIdentifier, schacHome, schacHomeGuest, preview);

        return given()
                .when()
                .contentType(ContentType.JSON)
                .body(registration)
                .put("/intake/api/register")
                .then();
    }
}