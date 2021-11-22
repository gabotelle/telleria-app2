package baseline;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FXMLControllerTest {

    @Test
    void validInput() {
        FXMLController fx = new FXMLController();
        assertTrue(fx.validInput("SmartPhone", "A-123-123-123", "899.97"));
    }

    @Test
    void getMonetaryValue() {
        FXMLController fx = new FXMLController();
        assertEquals("$900.00", fx.getMonetaryValue("900"));
    }

    @Test
    void addToObservableList() {
        FXMLController fx = new FXMLController();
        fx.addToObservableList("SmartPhone", "A-123-123-123", "899.99");

        assertTrue(fx.getObservableList().size() > 0);
    }

    @Test
    void isInList() {
        FXMLController fx = new FXMLController();
        fx.addToObservableList("SmartPhone", "A-123-123-123", "899.99");
        fx.addToObservableList("Laptop", "A-123-ABC-123", "2000");

        assertTrue(fx.isInList("SmartPhone", "aha"));
        assertTrue(fx.isInList("sjs", "A-123-ABC-123"));
    }

}