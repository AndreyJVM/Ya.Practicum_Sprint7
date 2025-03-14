import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CreateCourierTest {
    private Courier courier;
    private CourierClient courierClient;
    private ValidatableResponse response,
            loginResponse;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Successful courier creation")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createPositiveTest() {
        courier = DataGenerator.getRandom();
        response = courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));

        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(SC_OK, loginStatusCode);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_CREATED, statusCode);


        boolean isCreated = response.extract().path("ok");
        assertTrue(isCreated);

        courierId = loginResponse.extract().path("id");
        assertNotEquals(0, courierId);
    }

    @Test
    @DisplayName("Create courier with the same credentials")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createTwoSameCouriersTest() {
        courier = new Courier(DataGenerator.getRandom().getFirstName(),
                DataGenerator.getRandom().getLogin(),
                DataGenerator.getRandom().getPassword());
        courierClient.create(courier);
        response = courierClient.create(courier);
        loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");

        String bodyAnswer = response.extract().path("message");
        assertEquals("Этот логин уже используется. Попробуйте другой.", bodyAnswer);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_CONFLICT, statusCode);
    }

    @Test
    @DisplayName("Create courier with null Login")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createWithNullLoginTest() {
        courier = new Courier(DataGenerator.getRandom().getFirstName(),
                null,
                DataGenerator.getRandom().getPassword());
        response = courierClient.create(courier);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = response.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", bodyAnswer);
    }

    @Test
    @DisplayName("Create courier with null password")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createWithNullPasswordTest() {
        courier = new Courier(DataGenerator.getRandom().getFirstName(),
                DataGenerator.getRandom().getLogin(),
                null);
        response = courierClient.create(courier);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = response.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", bodyAnswer);
    }

    @Test
    @DisplayName("Create courier with null First Name")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createWithNullFirstNameTest() {
        courier = new Courier(null,
                DataGenerator.getRandom().getLogin(),
                DataGenerator.getRandom().getPassword());
        response = courierClient.create(courier);
        courierId = courierClient.login(CourierCredentials.from(courier)).extract().path("id");

        int statusCode = response.extract().statusCode();
        assertEquals(SC_CREATED, statusCode);

        boolean isCreated = response.extract().path("ok");
        assertTrue(isCreated);
    }

    @Test
    @DisplayName("Create courier with empty Login")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createWithEmptyLoginTest() {
        courier = new Courier(DataGenerator.getRandom().getFirstName(),
                "",
                DataGenerator.getRandom().getPassword());
        response = courierClient.create(courier);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = response.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", bodyAnswer);
    }

    @Test
    @DisplayName("Create courier with empty password")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createWithEmptyPasswordTest() {
        courier = new Courier(DataGenerator.getRandom().getFirstName(),
                DataGenerator.getRandom().getLogin(),
                "");
        response = courierClient.create(courier);

        int statusCode = response.extract().statusCode();
        assertEquals(SC_BAD_REQUEST, statusCode);

        String bodyAnswer = response.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", bodyAnswer);
    }
}