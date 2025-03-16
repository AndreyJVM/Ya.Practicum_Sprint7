import dto.Courier;
import api.CourierClient;
import api.DataGenerator;
import dto.CourierCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CourierLoginTest {

    private Courier courier;
    private CourierClient courierClient;
    private ValidatableResponse loginResponse;
    private int courierId;
    private final String NOT_FULL_AUTH_RESP_MESSAGE = "Недостаточно данных для входа";
    private final String NOT_FIND_ACCOUNT_RESP_MESSAGE = "Учетная запись не найдена";

    @Before
    public void setUp() {
        courier = DataGenerator.getRandomCourier();
        courierClient = new CourierClient();
        courierClient.create(courier);
    }

    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }

    @Test
    @DisplayName("Successful authorization")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierValidLoginTest() {
        loginResponse = courierClient.login(CourierCredentials.form(courier));

        loginResponse.statusCode(200);

        loginResponse.body("id", greaterThanOrEqualTo(0));
    }

    @Test
    @DisplayName("Empty login authorization")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierEmptyLoginTest() {
        loginResponse = courierClient
                .login(new CourierCredentials("", courier.getPassword()));
        courierId = courierClient
                .login(CourierCredentials.form(courier))
                .extract()
                .path("id");

        int statusCode = loginResponse
                .extract()
                .statusCode();

        assertEquals(400, statusCode);

        String bodyAnswer = loginResponse
                .extract()
                .path("message");

        assertEquals(NOT_FULL_AUTH_RESP_MESSAGE, bodyAnswer);
    }

    @Test
    @DisplayName("Empty password authorization")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierEmptyPasswordTest() {
        loginResponse = courierClient
                .login(new CourierCredentials(courier.getLogin(), ""));
        courierId = courierClient
                .login(CourierCredentials.form(courier))
                .extract()
                .path("id");

        int statusCode = loginResponse
                .extract()
                .statusCode();
        assertEquals(400, statusCode);

        String bodyAnswer = loginResponse
                .extract()
                .path("message");
        assertEquals(NOT_FULL_AUTH_RESP_MESSAGE, bodyAnswer);
    }

    @Test
    @DisplayName("Null field(login) authorization")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierNullLoginTest() {
        loginResponse = courierClient.login(new CourierCredentials(null, courier.getPassword()));
        courierId = courierClient
                .login(CourierCredentials.form(courier))
                .extract()
                .path("id");

        int statusCode = loginResponse
                .extract()
                .statusCode();
        assertEquals(400, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals(NOT_FULL_AUTH_RESP_MESSAGE, bodyAnswer);
    }

    @Test
    @DisplayName("Wrong login authorization")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierWrongLoginTest() {
        loginResponse = courierClient.login(new CourierCredentials("MGMT1989", courier.getPassword()));
        courierId = courierClient.login(CourierCredentials.form(courier)).extract().path("id");

        int statusCode = loginResponse.extract().statusCode();
        assertEquals(404, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals(NOT_FIND_ACCOUNT_RESP_MESSAGE, bodyAnswer);
    }

    @Test
    @DisplayName("Wrong password authorization")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierWrongPasswordTest() {
        loginResponse = courierClient.login(new CourierCredentials(courier.getLogin(), "mgmt23421"));
        courierId = courierClient.login(CourierCredentials.form(courier)).extract().path("id");

        int statusCode = loginResponse.extract().statusCode();
        assertEquals(404, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals(NOT_FIND_ACCOUNT_RESP_MESSAGE, bodyAnswer);
    }

    @Test
    @DisplayName("Authorization non-existent user")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierThatNotExists() {
        loginResponse = courierClient.login(new CourierCredentials("SpaceMonkey2006", "HDDssd777"));
        courierId = courierClient.login(CourierCredentials.form(courier)).extract().path("id");

        int statusCode = loginResponse.extract().statusCode();
        assertEquals(404, statusCode);

        String bodyAnswer = loginResponse.extract().path("message");
        assertEquals(NOT_FIND_ACCOUNT_RESP_MESSAGE, bodyAnswer);
    }

    private int response_200(ValidatableResponse response) {
        return response.extract().statusCode();
    }
}