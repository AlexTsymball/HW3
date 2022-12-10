package task2;

import lombok.AllArgsConstructor;
import lombok.ToString;
import task2.annotation.Property;

import java.time.Instant;

@ToString
@AllArgsConstructor
public class Test {
    private String stringProperty;

    @Property(name="number.Property")
    private int myNumber;

    @Property(name="numberProperty")
    private int id;


    @Property(format="dd.MM.yyyy HH:mm", name="")
    private Instant timeProperty;


    private Test() {
    }


    private void setId(int id) {
        this.id = id;
    }

    public int setId(String myNumber2) {
        return 2;
    }

    public void setId(double myNumber2) {
        System.out.println("test");
    }
    public void setStringProperty(String stringProperty) {
        this.stringProperty = stringProperty;
    }

    public void setMyNumber(int myNumber) {
        this.myNumber = myNumber;
    }

    public void setTimeProperty(Instant timeProperty) {
        this.timeProperty = timeProperty;
    }
}
