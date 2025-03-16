import api.OrderClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class OrderListTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Get not empty order list")
    @Description("Basic test for get request to endpoint /api/v1/orders")
    public void orderListNotEmptyTest() {
        ValidatableResponse response = orderClient.getOrderList();

        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);

        List<String> bodyAnswer = response.extract().path("orders");
        assertFalse(bodyAnswer.isEmpty());
    }

    @Test
    @DisplayName("Get not null order list")
    @Description("Basic test for get request to endpoint /api/v1/orders")
    public void orderListNotNullTest() {
        ValidatableResponse response = orderClient.getOrderList();

        int statusCode = response.extract().statusCode();
        assertEquals(SC_OK, statusCode);

        List<String> bodyAnswer = response.extract().path("orders");
        assertNotEquals(null, bodyAnswer);
    }
}