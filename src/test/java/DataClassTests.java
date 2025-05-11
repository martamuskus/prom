import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataClassTests {
    @Test
    void testGetPaymentMethodInfo() {
        HashMap<String, Pair<Integer, Double>> pm = new HashMap<>();
        pm.put("CARD1", new Pair<>(10, 100.0));
        Data data = new Data(new HashMap<>(), pm);

        Pair<Integer, Double> result = data.get_payment_method_info("CARD1");
        assertEquals(10, result.getFirst());
        assertEquals(100.0, result.getSecond());
    }

    @Test
    void testGetOrderInfo() {
        HashSet<String> methods = new HashSet<>(Arrays.asList("PUNKTY", "CARD1"));
        HashMap<String, Pair<Double, HashSet<String>>> orders = new HashMap<>();
        orders.put("ORD1", new Pair<>(200.0, methods));
        Data data = new Data(orders, new HashMap<>());

        Pair<Double, HashSet<String>> result = data.get_order_info("ORD1");
        assertEquals(200.0, result.getFirst());
        assertTrue(result.getSecond().contains("CARD1"));
    }

    @Test
    void testUpdatePaymentLimit() {
        HashMap<String, Pair<Integer, Double>> pm = new HashMap<>();
        pm.put("CARD1", new Pair<>(10, 100.0));
        Data data = new Data(new HashMap<>(), pm);

        data.updatePaymentLimit("CARD1", 30.0);
        assertEquals(70.0, data.get_payment_method_info("CARD1").getSecond());
    }
}


