package api.auth;

import api.DataGenerator;
import dto.Courier;
import dto.CourierCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

public class CreateCourierTest extends BaseTest{

    private ValidatableResponse response, loginResponse;
    private static final String LOGIN_ALREADY_USE = "Этот логин уже используется. Попробуйте другой.";
    private static final String NOT_FULL_DATA = "Недостаточно данных для создания учетной записи";
    private int courierId;

    @Test
    @DisplayName("Successful courier creation")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createPositiveTest() {
        courier = DataGenerator.getRandomCourier();

        courierClient
                .create(courier)
                .statusCode(201)
                .body("ok", equalTo(true));

        courierClient
                .login(CourierCredentials.form(courier))
                .statusCode(200)
                .body("id", greaterThanOrEqualTo(0));
    }

    @Test
    @DisplayName("Create courier with the same credentials")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createTwoSameCouriersTest() {
        courier = new Courier(DataGenerator.getRandomCourier().getFirstName(),
                DataGenerator.getRandomCourier().getLogin(),
                DataGenerator.getRandomCourier().getPassword());
        courierClient.create(courier);

        response = courierClient.create(courier);

        loginResponse = courierClient.login(CourierCredentials.form(courier));

        response
                .body("message", equalTo(LOGIN_ALREADY_USE))
                .statusCode(409);

        courierId = loginResponse.extract().path("id");
    }

    @Test
    @DisplayName("Create courier with null Login")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createWithNullLoginTest() {
        courier = new Courier(DataGenerator.getRandomCourier().getFirstName(),
                null,
                DataGenerator.getRandomCourier().getPassword());
        courierClient
                .create(courier)
                .statusCode(400)
                .body("message", equalTo(NOT_FULL_DATA));
    }

    @Test
    @DisplayName("Create courier with null password")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createWithNullPasswordTest() {
        courier = new Courier(DataGenerator.getRandomCourier().getFirstName(),
                DataGenerator.getRandomCourier().getLogin(),
                null);

        courierClient.create(courier)
                .statusCode(400)
                .body("message", equalTo(NOT_FULL_DATA));
    }

    @Test
    @DisplayName("Create courier with null First Name")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createWithNullFirstNameTest() {
        courier = new Courier(null,
                DataGenerator.getRandomCourier().getLogin(),
                DataGenerator.getRandomCourier().getPassword());

        courierClient
                .create(courier)
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = courierClient.login(CourierCredentials.form(courier)).extract().path("id");
    }

    @Test
    @DisplayName("Create courier with empty Login")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createWithEmptyLoginTest() {
        courier = new Courier(DataGenerator.getRandomCourier().getFirstName(),
                "",
                DataGenerator.getRandomCourier().getPassword());

        courierClient
                .create(courier)
                .statusCode(400)
                .body("message", equalTo(NOT_FULL_DATA));
    }

    @Test
    @DisplayName("Create courier with empty password")
    @Description("Basic test for post request to endpoint /api/v1/courier")
    public void createWithEmptyPasswordTest() {
        courier = new Courier(DataGenerator.getRandomCourier().getFirstName(),
                DataGenerator.getRandomCourier().getLogin(),
                "");

        courierClient
                .create(courier)
                .statusCode(400)
                .body("message", equalTo(NOT_FULL_DATA));
    }
}