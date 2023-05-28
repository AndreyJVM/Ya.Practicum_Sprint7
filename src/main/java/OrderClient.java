import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {

    private static final String ORDER_CREATE_URL ="/api/v1/orders";

    @Step("Create order")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(ORDER_CREATE_URL)
                .then();
    }

    @Step("Get order list")
    public ValidatableResponse getOrderList() {
        return given()
                .spec(getSpec())
                .when()
                .get(ORDER_CREATE_URL)
                .then();
    }
}
