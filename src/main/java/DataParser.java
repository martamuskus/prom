import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;

public class DataParser {
    private final String ordersFile;
    private final String paymentsFile;

    DataParser(String ordersFilename, String paymentMethodsFilename) {
        this.ordersFile = ordersFilename;
        this.paymentsFile = paymentMethodsFilename;
    }

    public Data parseFiles() {
        HashMap<String, Pair<Double, HashSet<String>>> orders = new HashMap<>();
        HashMap<String, Pair<Integer, Double>> payments = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);

        try {
            File ordersFileObj = new File(ordersFile);
            if (!ordersFileObj.exists()) {
                throw new IOException("Orders file does not exist: " + ordersFile);
            }

            List<OrderEl> orderEls = mapper.readValue(
                    ordersFileObj,
                    new TypeReference<>() {}
            );

            //sorting orders by val
            orderEls.sort((a, b) -> Double.compare(b.getValue(), a.getValue()));

            LinkedHashMap<String, Pair<Double, HashSet<String>>> sortedOrders = new LinkedHashMap<>();
            for (OrderEl orderEl : orderEls) {
                Pair<Double, HashSet<String>> el = new Pair<>(
                        orderEl.getValue(),
                        orderEl.getPromotions()
                );
                sortedOrders.put(orderEl.getID(), el);
            }

            File paymentsFileObj = new File(paymentsFile);
            if (!paymentsFileObj.exists()) {
                throw new IOException("Payments file does not exist: " + paymentsFile);
            }

            List<PaymentEl> paymentEls = mapper.readValue(
                    paymentsFileObj,
                    new TypeReference<>() {}
            );

            for (PaymentEl paymentEl : paymentEls) {
                assert(paymentEl.getDiscount() >= 0 && paymentEl.getDiscount() <= 100);
                Pair<Integer, Double> el = new Pair<>(
                        paymentEl.getDiscount(),
                        paymentEl.getLimit()
                );
                payments.put(paymentEl.getID(), el);
            }
            return new Data(sortedOrders, payments);
        }
        catch (StreamReadException | DatabindException e) {
            throw new IllegalArgumentException("Error parsing JSON file." + e.getMessage());
        }
        catch (IOException e) {
            throw new IllegalArgumentException("File error: " + e.getMessage());
        }
        catch (Exception e) {
            throw new IllegalArgumentException("Error" + e.getMessage());
        }
    }
}
