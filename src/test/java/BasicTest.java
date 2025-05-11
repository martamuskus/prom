import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BasicTest {

    @Test
    void testOnlyPointsFullDiscount() {
        HashMap<String, Pair<Double, HashSet<String>>> orders = new HashMap<>();
        HashSet<String> allowed = new HashSet<>();
        allowed.add("PUNKTY");
        orders.put("order1", new Pair<>(100.0, allowed));

        HashMap<String, Pair<Integer, Double>> payment_methods = new HashMap<>();
        payment_methods.put("PUNKTY", new Pair<>(30, 200.0));

        Data data = new Data(orders, payment_methods);

        try {
            HashMap<String, Double> result = Main.processOrders(data);

            HashMap<String, Double> expected = new HashMap<>();
            expected.put("PUNKTY", 70.0);

            assertEquals(expected.size(), result.size(), "Incorrect number of entries");
            for (String key : expected.keySet()) {
                assertTrue(result.containsKey(key), "Missing key: " + key);
                assertEquals(expected.get(key), result.get(key), 1e-6, "Wrong value for key: " + key);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Test
    void testCardOnlyPayment() {
        HashMap<String, Pair<Double, HashSet<String>>> orders = new HashMap<>();
        HashSet<String> allowed = new HashSet<>();
        allowed.add("CARD1");
        orders.put("order2", new Pair<>(100.0, allowed));

        HashMap<String, Pair<Integer, Double>> payment_methods = new HashMap<>();
        payment_methods.put("PUNKTY", new Pair<>(30, 50.0));
        payment_methods.put("CARD1", new Pair<>(20, 150.0));
        payment_methods.put("CARD2", new Pair<>(10, 100.0));

        Data data = new Data(orders, payment_methods);

        try {
            HashMap<String, Double> result = Main.processOrders(data);
            HashMap<String, Double> expected = new HashMap<>();
            expected.put("CARD1", 80.0);

            assertEquals(expected.size(), result.size(), "Incorrect number of entries");
            for (String key : expected.keySet()) {
                assertTrue(result.containsKey(key), "Missing key: " + key);
                assertEquals(expected.get(key), result.get(key), 1e-6, "Wrong value for key: " + key);
            }
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
