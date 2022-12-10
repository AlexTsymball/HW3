import classForTest.*;
import org.junit.jupiter.api.Test;
import task2.Task2;
import task2.exceptions.*;

import java.nio.file.Path;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Task2Test {

    static final Path PATH_TO_PROPERTIES = Path.of("src/main/resources/task2.properties");


    @Test
    public void testWithIncorrectFieldNameAndWithoutAnnotation(){
        NullPointerException exception = assertThrows(NullPointerException.class, () ->{
            Task2.loadFromProperties(TestClassWithIncorrectFieldNameAndWithoutAnnotation.class, PATH_TO_PROPERTIES);
        });
        assertEquals(exception.getMessage(), "There is no myNumber field in properties file");
    }

    @Test
    public void testWithIncorrectFieldType(){
        WrongTypeException exception = assertThrows(WrongTypeException.class, () ->{
            Task2.loadFromProperties(TestClassWithIncorrectFieldType.class, PATH_TO_PROPERTIES);
        });
        assertEquals(exception.getMessage(), "Type of fields must be only String, int, Integer or Instant. Not double");
    }

    @Test
    public void testWithIncorrectFormat(){
        DateFormatException exception = assertThrows(DateFormatException.class, () ->{
            Task2.loadFromProperties(TestClassWithIncorrectFormat.class, PATH_TO_PROPERTIES);
        });
        assertEquals(exception.getMessage(), "Can't parse date. Wrong format or date/time");
    }

    @Test
    public void testWithoutEmptyConstructor(){
        NoEmptyConstructorException exception = assertThrows(NoEmptyConstructorException.class, () ->{
            Task2.loadFromProperties(TestClassWithoutEmptyConstructor.class, PATH_TO_PROPERTIES);
        });
        assertEquals(exception.getMessage(), "There is no empty constructor");
    }

    @Test
    public void testWithoutSetter(){
        NoSuchSetterException exception = assertThrows(NoSuchSetterException.class, () ->{
            Task2.loadFromProperties(TestClassWithoutSetter.class, PATH_TO_PROPERTIES);
        });
        assertEquals(exception.getMessage(), "There is problem with set value for  stringProperty");
    }

    @Test
    public void testWithPrivateConstructor(){
        TestClassWithPrivateConstructor actual = Task2.loadFromProperties(TestClassWithPrivateConstructor.class, PATH_TO_PROPERTIES);
        TestClassWithPrivateConstructor expected =
                new TestClassWithPrivateConstructor("value1", 10, Instant.parse("2022-11-29T18:30:00Z"));
        assertEquals(expected, actual);
    }

    @Test
    public void testWithPrivateSetter(){
        TestClassWithPrivateSetter actual = Task2.loadFromProperties(TestClassWithPrivateSetter.class, PATH_TO_PROPERTIES);
        TestClassWithPrivateSetter expected =
                new TestClassWithPrivateSetter("value1", 10, Instant.parse("2022-11-29T18:30:00Z"));
        assertEquals(expected, actual);
    }

    @Test
    public void testWithNullParameters(){
        NullPointerException exception = assertThrows(NullPointerException.class, () ->{
            Task2.loadFromProperties(null,null);
        });
        assertEquals(exception.getMessage(), "clz and propertiesPath must be notNull");
    }


}
