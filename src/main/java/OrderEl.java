import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class OrderEl {
    private final String id;
    private final Double value;
    private final HashSet<String> promotions;

    @JsonCreator
    public OrderEl(
            @JsonProperty("id") String id,
            @JsonProperty("value") Double value,
            @JsonProperty("promotions") HashSet<String> promotions
    )
    {
        if (id == null || value == null) {
            throw new IllegalArgumentException("arguments must not be null");
        }

        this.id = id;
        this.value = value;
        this.promotions = promotions;
    }

    public String getID() {
        return id;
    }

    public Double getValue() {
        return value;
    }

    public HashSet<String> getPromotions() {
        return promotions;
    }
}