package api.order;

import api.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

public class OrderListTest {

    private OrderClient orderClient;
    private ValidatableResponse response;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Get not empty order list")
    @Description("Basic test for get request to endpoint /api/v1/orders")
    public void orderListNotEmptyTest() {
        response = orderClient.getOrderList()
                .statusCode(200);

        List<String> bodyAnswer = response.extract().path("orders");
        assertFalse(bodyAnswer.isEmpty());
    }

    @Test
    @DisplayName("Get not null order list")
    @Description("Basic test for get request to endpoint /api/v1/orders")
    public void orderListNotNullTest() {
        response = orderClient.getOrderList()
                .statusCode(200)
                .body("orders", not(nullValue()))
                .body("orders", hasSize(greaterThanOrEqualTo(0)));
    }
}