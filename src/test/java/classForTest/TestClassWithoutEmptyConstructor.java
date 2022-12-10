package classForTest;

import lombok.AllArgsConstructor;
import lombok.Setter;
import task2.annotation.Property;

import java.time.Instant;

@Setter
@AllArgsConstructor
public class TestClassWithoutEmptyConstructor {
    private String stringProperty;

    @Property(name = "numberProperty")
    private int myNumber;

    @Property(format="dd.MM.yyyy HH:mm")
    private Instant timeProperty;


}
