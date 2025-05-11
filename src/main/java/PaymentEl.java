import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
public class PaymentEl {
    private final String id;
    private final Integer discount;
    private final Double limit;

    public PaymentEl(@JsonProperty("id") String id,
                     @JsonProperty("discount") Integer discount,
                     @JsonProperty("limit") Double limit) {
        if (id == null || discount == null || limit == null) {
            throw new IllegalArgumentException("Missing required field");
        }
        this.id = id;
        this.discount = discount;
        this.limit = limit;
    }

    public String getID() {
        return id;
    }

    public int getDiscount() {
        return discount;
    }

    public Double getLimit() {
        return limit;
    }
}

