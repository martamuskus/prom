import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Data {
    //{id<String>: {value, [list of promotions]}}
    private final HashMap<String, Pair<Double, HashSet<String>>> orders;
    //{id<String>: {discount, limit}}
    private final HashMap<String, Pair<Integer, Double>> payment_methods;

    Data(HashMap<String, Pair<Double, HashSet<String>>> orders, HashMap<String, Pair<Integer, Double>> payment_methods) {
        this.orders = orders;
        this.payment_methods = payment_methods;
    }

    public Pair<Integer, Double> get_payment_method_info(String payment_method) {
        return payment_methods.get(payment_method); //returns discount + limit
    }

    public Pair<Double, HashSet<String>> get_order_info(String order) {
        return orders.get(order);
    }

    public ArrayList<String> getPaymentMethods() {
        return new ArrayList<>(payment_methods.keySet());
    }

    public HashMap<String, Pair<Double, HashSet<String>>> get_orders() {
        return orders;
    }

    //removing order which has been fully paid for
    public void deleteOrder(String order) {
        orders.remove(order);
    }

    //updating limit for a given payment
    public void updatePaymentLimit(String payment_method, Double val) {
        Integer discount = payment_methods.get(payment_method).getFirst();
        Double limit = payment_methods.get(payment_method).getSecond();
        payment_methods.put(payment_method, new Pair<>(discount, limit-val));
    }
}
