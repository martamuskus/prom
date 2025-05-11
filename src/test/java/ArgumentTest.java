import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class ArgumentTest {

    @Test
    public void testTooFewArguments() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        Main.main(new String[]{"file1.json"});

        String output = errContent.toString();
        assertTrue(output.contains("Two arguments"), "Expected error about too few arguments");

        System.setErr(System.err);
    }

    @Test
    public void testNonexistentOrdersFile() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        Main.main(new String[]{"nonexistent_orders.json", "file2.json"});

        String output = errContent.toString();
        assertTrue(output.contains("Orders file does not exist"), "Expected error about missing orders file");

        System.setErr(System.err);
    }

    @Test
    public void testBothFilesNonexistent() {
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));

        Main.main(new String[]{"no1.json", "no2.json"});

        String output = errContent.toString();
        assertTrue(output.contains("Orders file does not exist"), "Should report missing orders file");

        System.setErr(System.err);
    }

}
