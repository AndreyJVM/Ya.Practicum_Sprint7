package api;

import dto.Client;
import dto.Courier;
import dto.CourierCredentials;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;

public class CourierClient extends Client {

    private static final String COURIER_PATH = "/api/v1/courier/";

    @Step("Login dto.Courier")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(COURIER_PATH + "login")
                .then();
    }

    @Step("Create dto.Courier")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Delete dto.Courier")
    public ValidatableResponse delete(int courierId) {
        return given()
                .spec(getSpec())
                .when()
                .delete(COURIER_PATH + courierId)
                .then();
    }
}