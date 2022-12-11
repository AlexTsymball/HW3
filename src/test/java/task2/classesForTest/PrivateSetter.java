package task2.classesForTest;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import task2.annotation.Property;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PrivateSetter {
    private String stringProperty;

    @Property(name = "numberProperty")
    private int myNumber;

    @Property(format="dd.MM.yyyy HH:mm")
    private Instant timeProperty;

    private void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public void setMyNumber(int myNumber) {
        this.myNumber = myNumber;
    }

    public void setTimeProperty(Instant timeProperty) {
        this.timeProperty = timeProperty;
    }
}
