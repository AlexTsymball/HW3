package task2;

import task2.annotation.Property;

import java.time.Instant;

public class Test {
    private String stringProperty;

    @Property(name="number.Property")
    private int myNumber;

    @Property(name="numberProperty")
    private int myNumber2;


    @Property(format="dd.MM.yyyy HH:mm", name="")
    private Instant timeProperty;


    @Override
    public String toString() {
        return "Test{" +
                "stringProperty='" + stringProperty + '\'' +
                ", myNumber=" + myNumber +
                ", myNumber2=" + myNumber2 +
                ", timeProperty=" + timeProperty +
                '}';
    }

    private Test() {
    }

    public Test(String stringProperty, int myNumber, int myNumber2, Instant timeProperty) {
        this.stringProperty = stringProperty;
        this.myNumber = myNumber;
        this.myNumber2 = myNumber2;
        this.timeProperty = timeProperty;
    }

    private void setMyNumber2(int myNumber2) {
        this.myNumber2 = myNumber2;
    }
    public int setMyNumber2(String myNumber2) {
        System.out.println("sii");
        return 2;
    }
    public void setMyNumber2(double myNumber2) {
        System.out.println("s");
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
