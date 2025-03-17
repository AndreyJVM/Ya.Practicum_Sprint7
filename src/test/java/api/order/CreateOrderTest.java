package api.order;

import api.DataGenerator;
import api.OrderClient;
import dto.Order;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@RunWith(Parameterized.class)
public class CreateOrderTest {

    private Order order;
    private OrderClient orderClient;
    private final String[] color;
    private ValidatableResponse response;

    public CreateOrderTest(String color) {
        this.color = new String[]{color};
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Parameterized.Parameters(name = "color1: {0}; color2: {1}")
    public static Object[][] setColor() {
        return new Object[][]{
                {"BLACK"},
                {"GREY"},
                {""},
                {"BLACK , GREY"}
        };
    }

    @Test
    @DisplayName("Create order")
    @Description("Basic test for post request to endpoint /api/v1/orders")
    public void createOrderPositiveTest() {
        order = DataGenerator.getRandomWithoutColor(color);
        orderClient
                .create(order)
                .statusCode(201)
                .body("track", greaterThanOrEqualTo(0));
    }
}