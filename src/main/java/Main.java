import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class Main {
    public static void addPayment(HashMap<String, Double> payments, String payment, Double val) {
        if (payments.containsKey(payment)) {
            payments.put(payment, payments.get(payment) + val);
        }
        else {
            payments.put(payment, val);
        }
    }

    public static HashMap<String, Double> processOrders(Data data) throws Exception {
        HashMap<String, Double> payedAmounts = new HashMap<>();
        ArrayList<String> availablePayments = data.getPaymentMethods();
        /*adding orders which were paid for partially with points and
        partially with card/cards */
        HashMap<String, ArrayList<Pair<String, Double>>> payed_mixed = new HashMap<>();

        Integer points_discount = data.get_payment_method_info("PUNKTY").getFirst();
        if (points_discount == null) {
            points_discount = 0;
        }

        /* For each order, ordered descending by price we consider three different approaches:
        1. paying fully with points and applying that discount
        2. paying fully with card with the most favourable discount
        3. paying with at least 10% of points and rest with some card (or
        with less than 10% points and no discount).
         */
        for (String order : data.get_orders().keySet()) {
            Double value = data.get_orders().get(order).getFirst();
            HashSet<String> payments = data.get_orders().get(order).getSecond();

            double price1, price2, price3;
            Double current_point_limit = data.get_payment_method_info("PUNKTY").getSecond();
            if (current_point_limit >= value) {
                price1 = value * (100 - points_discount) / 100;
            } else {
                price1 = value;
            }

            int best_discount = 0;
            String best_payment = null;
            if (payments != null) {
                for (String payment : payments) {
                    if (payment.equals("PUNKTY")) continue;
                    Integer discount = data.get_payment_method_info(payment).getFirst();
                    Double current_limit = data.get_payment_method_info(payment).getSecond();
                    if (discount > best_discount && current_limit >= value) {
                        best_discount = discount;
                        best_payment = payment;
                    }
                }
            }
            price2 = value * (100 - best_discount) / 100;

            Double left;
            ArrayList<Pair<String, Double>> payed = new ArrayList<>();
            if (current_point_limit >= 0.1 * value) {
                left = 0.9 * value;
                payed.add(new Pair<>("PUNKTY", 0.1 * value));
            }
            else {
                left = value;
            }
            price3 = left;

            for (String payment : availablePayments) {
                if (payment.equals("PUNKTY")) continue;
                Double current_limit = data.get_payment_method_info(payment).getSecond();
                if (current_limit >= left) {
                    payed.add(new Pair<>(payment, left));
                    break;
                } else {
                    payed.add(new Pair<>(payment, current_limit));
                    left -= current_limit;
                }
            }

            double best = Math.min(Math.min(price1, price2), price3);
            //Finding the best option. If two approaches give the same result we
            //prioritize paying with points over paying with card.
            if (best == price1) {
                addPayment(payedAmounts, "PUNKTY", price1);
                data.updatePaymentLimit("PUNKTY", price1);
            }
            else if (best == price2 && best_payment != null) {
                addPayment(payedAmounts, best_payment, price2);
                data.updatePaymentLimit(best_payment, price2);
            }
            else {
                payed_mixed.put(order, payed);
                for (Pair<String, Double> el : payed) {
                    addPayment(payedAmounts, el.getFirst(), el.getSecond());
                    data.updatePaymentLimit(el.getFirst(), el.getSecond());
                }
            }
        }

        /*If points were left, just subbing them wherever possible instead of
        paying with only 10% of points */
        double points_left = data.get_payment_method_info("PUNKTY").getSecond();
        if (points_left > 0 && !payed_mixed.isEmpty()) {
            for (String order : payed_mixed.keySet()) {
                ArrayList<Pair<String, Double>> payed = payed_mixed.get(order);
                for (Pair<String, Double> el : payed) {
                    if (el.getFirst().equals("PUNKTY")) continue;
                    Double price = el.getSecond();
                    //swapping some part of paying with card to paying with points
                    if (price >= points_left) {
                        addPayment(payedAmounts, "PUNKTY", points_left);
                        addPayment(payedAmounts, el.getFirst(), -points_left);
                        points_left = 0;
                        break;
                    }
                    else {
                        addPayment(payedAmounts, "PUNKTY", price);
                        addPayment(payedAmounts, el.getFirst(), -price);
                        points_left -= price;
                    }
                }
                if (points_left == 0) break;
            }
        }
        return payedAmounts;
    }

    public static void main(String[] args) {
        try {
            if (args.length != 2) {
                throw new IllegalArgumentException("Two arguments required: <orders file> <payment methods file>");
            }
            String ordersFile = args[0];
            String paymentMethodsFile = args[1];
            Data data = (new DataParser(ordersFile, paymentMethodsFile)).parseFiles();

            HashMap<String, Double> payedAmounts = processOrders(data);
            for (String id : payedAmounts.keySet()) {
                System.out.println(id + " " + payedAmounts.get(id));
            }
        }
        catch (IllegalArgumentException e) {
            System.err.println("Illegal Argument Exception: " + e.getMessage());
        }
        catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}
