import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderParserTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testValidOrderParsing() throws Exception {
        String json = """
        [
          { "id": "ORDER1", "value": "150.00", "promotions": ["mZysk", "BosBankrut"] },
          { "id": "ORDER2", "value": "99.99", "promotions": [] }
        ]
        """;

        File file = createTempJsonFile(json);
        List<OrderEl> result = mapper.readValue(file, new TypeReference<>() {});
        assertEquals(2, result.size());
        assertEquals("ORDER1", result.getFirst().getID());
        assertEquals(150.00, result.getFirst().getValue());
        assertEquals(2, result.getFirst().getPromotions().size());
    }

    @Test
    public void testMissingPromotionsField() {
        String json = """
        [
          { "id": "ORDER1", "value": "150.00" }
        ]
        """;

        File file = createTempJsonFile(json);
        assertThrows(Exception.class, () -> {
            mapper.readValue(file, new TypeReference<List<OrderEl>>() {});
        });
    }

    @Test
    public void testInvalidValueFormat() {
        String json = """
        [
          { "id": "ORDER1", "value": "notANumber", "promotions": ["a"] }
        ]
        """;

        File file = createTempJsonFile(json);
        assertThrows(Exception.class, () -> {
            mapper.readValue(file, new TypeReference<List<OrderEl>>() {});
        });
    }

    private File createTempJsonFile(String json) {
        try {
            File tempFile = File.createTempFile("orderTest", ".json");
            FileWriter writer = new FileWriter(tempFile);
            writer.write(json);
            writer.close();
            return tempFile;
        } catch (Exception e) {
            fail("Failed to create temp file: " + e.getMessage());
            return null;
        }
    }
}

