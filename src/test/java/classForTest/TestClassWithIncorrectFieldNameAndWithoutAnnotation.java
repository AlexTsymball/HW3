package classForTest;

import lombok.NoArgsConstructor;
import lombok.Setter;
import task2.annotation.Property;

import java.time.Instant;

@Setter
@NoArgsConstructor
public class TestClassWithIncorrectFieldNameAndWithoutAnnotation {
    private String stringProperty;

    private int myNumber;

    @Property(format="dd.MM.yyyy HH:mm")
    private Instant timeProperty;

}
