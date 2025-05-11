import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class PaymentParserTest {
    private final ObjectMapper mapper = new ObjectMapper();


    @Test
    public void testValidJsonParsing() throws Exception {
        String json = """
        [
          { "id": "PUNKTY", "discount": 15, "limit": 120.00 },
          { "id": "mZysk", "discount": 10, "limit": 180.00 }
        ]
        """;

        File file = createTempJsonFile(json);
        List<PaymentEl> result = mapper.readValue(file, new TypeReference<>() {});

        assertEquals(2, result.size());
        assertEquals("PUNKTY", result.getFirst().getID());
        assertEquals(15, result.getFirst().getDiscount());
        assertEquals(120.00, result.getFirst().getLimit());
    }

    @Test
    public void testWrongDataType() {
        String json = """
        [
          { "id": "PUNKTY", "discount": "aaaa", "limit": 120.00 }
        ]
        """;

        File file = createTempJsonFile(json);
        assertThrows(Exception.class, () -> {
            mapper.readValue(file, new TypeReference<List<PaymentEl>>() {});
        });
    }

    @Test
    public void testMissingField() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, true);
        String json = """
        [
          {"limit": 120.00 }
        ]
        """;

        File file = createTempJsonFile(json);
        assertThrows(Exception.class, () -> {
            mapper.readValue(file, new TypeReference<List<PaymentEl>>() {});
        });
    }

    @Test
    public void testInvalidJsonFormat() {
        String json = """
          { "id": "PUNKTY", "discount": 15, "limit": 120.00 }
          { "id": "mZysk", "discount": 10, "limit": 180.00 }
        """;

        File file = createTempJsonFile(json);
        assertThrows(Exception.class, () -> {
            mapper.readValue(file, new TypeReference<List<PaymentEl>>() {});
        });
    }

    //creating tmp json file
    private File createTempJsonFile(String json) {
        try {
            File tempFile = File.createTempFile("test", ".json");
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
