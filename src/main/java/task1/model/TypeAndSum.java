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
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TypeAndSum {

    private String type;

    @JsonSerialize(using = CustomDoubleSerializer.class)
    @JsonProperty("sum")
    private Double sumFineAmount;
}
