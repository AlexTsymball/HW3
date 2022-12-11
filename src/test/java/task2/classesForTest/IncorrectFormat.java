package task2.classesForTest;

import lombok.NoArgsConstructor;
import lombok.Setter;
import task2.annotation.Property;

import java.time.Instant;

@Setter
@NoArgsConstructor
public class IncorrectFormat {
    private String stringProperty;

    @Property(name = "numberProperty")
    private int myNumber;

    @Property(format="dd.MM HH:mm")
    private Instant timeProperty;
}
