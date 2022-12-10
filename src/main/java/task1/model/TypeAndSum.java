package task1.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import task1.serializer.CustomDoubleSerializer;


@JsonAutoDetect
@JsonPropertyOrder({"type", "sumFineAmount"})
@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TypeAndSum {

    private String type;

    @JsonSerialize(using = CustomDoubleSerializer.class)
    @JsonProperty("fine_amount")
    private Double sumFineAmount;

    @Override
    public String toString() {
        return "TypeAndSum{" +
                "type='" + type + '\'' +
                ", sumFineAmount=" + sumFineAmount +
                '}';
    }

    public synchronized void setType(String type) {
        this.type = type;
    }

    public synchronized void setSumFineAmount(Double sumFineAmount) {
        this.sumFineAmount = sumFineAmount;
    }
}
