import dto.Courier;
import api.CourierClient;
import api.DataGenerator;
import dto.CourierCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class CourierLoginTest {

    private Courier courier;
    private CourierClient courierClient;
    private final static String EMPTY_STRING = "";
    private final static String NOT_FULL_AUTH_RESP_MESSAGE = "Недостаточно данных для входа";
    private final static String NOT_FIND_ACCOUNT_RESP_MESSAGE = "Учетная запись не найдена";

    @Before
    public void setUp() {
        courier = DataGenerator.getRandomCourier();
        courierClient = new CourierClient();
        courierClient.create(courier);
    }

    @After
    public void cleanUp() {
        courierClient.delete(getIdCourierClient());
    }

    @Test
    @DisplayName("Successful authorization")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierValidLoginTest() {
        courierClient
                .login(CourierCredentials.form(courier))
                .statusCode(200)
                .body("id", greaterThanOrEqualTo(0));
    }

    @Test
    @DisplayName("Empty login authorization")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierEmptyLoginTest() {
        courierClient
                .login(new CourierCredentials(EMPTY_STRING, courier.getPassword()))
                .statusCode(400)
                .body("message", equalTo(NOT_FULL_AUTH_RESP_MESSAGE));
    }

    @Test
    @DisplayName("Empty password authorization")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierEmptyPasswordTest() {
        courierClient
                .login(new CourierCredentials(courier.getLogin(), EMPTY_STRING))
                .statusCode(400)
                .body("message", equalTo(NOT_FULL_AUTH_RESP_MESSAGE));
    }

    @Test
    @DisplayName("Null field(login) authorization")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierNullLoginTest() {
        courierClient
                .login(new CourierCredentials(null, courier.getPassword()))
                .statusCode(400)
                .body("message", equalTo(NOT_FULL_AUTH_RESP_MESSAGE));
    }

    @Test
    @DisplayName("Wrong login authorization")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierWrongLoginTest() {
        courierClient
                .login(new CourierCredentials("MGMT1989", courier.getPassword()))
                .statusCode(404)
                .body("message", equalTo(NOT_FIND_ACCOUNT_RESP_MESSAGE));
    }

    @Test
    @DisplayName("Wrong password authorization")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierWrongPasswordTest() {
        courierClient
                .login(new CourierCredentials(courier.getLogin(), "mgmt23421"))
                .statusCode(404)
                .body("message", equalTo(NOT_FIND_ACCOUNT_RESP_MESSAGE));
    }

    @Test
    @DisplayName("Authorization non-existent user")
    @Description("Basic test for post request to endpoint /api/v1/courier/login")
    public void courierThatNotExists() {
        courierClient
                .login(new CourierCredentials("SpaceMonkey2006", "HDDssd777"))
                .statusCode(404)
                .body("message", equalTo(NOT_FIND_ACCOUNT_RESP_MESSAGE));
    }

    private int getIdCourierClient(){
        return courierClient
                .login(CourierCredentials.form(courier))
                .extract()
                .path("id");
    }
}