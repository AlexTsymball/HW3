package classForTest;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import task2.annotation.Property;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TestClassWithoutSetter {
    private String stringProperty;

    @Property(name = "numberProperty")
    private int myNumber;

    @Property(format="dd.MM.yyyy HH:mm")
    private Instant timeProperty;


}
