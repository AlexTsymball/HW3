package classForTest;

import lombok.*;
import task2.annotation.Property;

import java.time.Instant;

@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class TestClassWithPrivateConstructor {
    private String stringProperty;

    @Property(name = "numberProperty")
    private int myNumber;

    @Property(format="dd.MM.yyyy HH:mm")
    private Instant timeProperty;

    private TestClassWithPrivateConstructor() {
    }
}
