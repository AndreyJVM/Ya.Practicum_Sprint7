package api.auth;

import api.CourierClient;
import dto.Courier;
import dto.CourierCredentials;
import org.junit.After;
import org.junit.Before;

public class BaseTest {

    protected CourierClient courierClient;
    protected Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void down() {
        courierClient.delete(getIdCourierClient(courier));
    }

    private int getIdCourierClient(Courier courier){
        return courierClient
                .login(CourierCredentials.form(courier))
                .extract()
                .path("id");
    }
}
