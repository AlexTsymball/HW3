package task2;

import task2.classesForTest.*;
import org.junit.jupiter.api.Test;
import task2.exceptions.*;

import java.nio.file.Path;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class Task2Test {

    static final Path PATH_TO_PROPERTIES = Path.of("src/main/resources/task2test.properties");


    @Test
    public void testIncorrectFieldNameAndWithoutAnnotation(){
        NullPointerException exception = assertThrows(NullPointerException.class, () ->{
            Task2.loadFromProperties(IncorrectFieldNameAndWithoutAnnotation.class, PATH_TO_PROPERTIES);
        });
        assertEquals(exception.getMessage(), "There is no myNumber field in properties file");
    }

    @Test
    public void testIncorrectFieldType(){
        WrongTypeException exception = assertThrows(WrongTypeException.class, () ->{
            Task2.loadFromProperties(IncorrectFieldType.class, PATH_TO_PROPERTIES);
        });
        assertEquals(exception.getMessage(), "Type of fields must be only String, int, Integer or Instant. Not double");
    }

    @Test
    public void testIncorrectFormat(){
        DateFormatException exception = assertThrows(DateFormatException.class, () ->{
            Task2.loadFromProperties(IncorrectFormat.class, PATH_TO_PROPERTIES);
        });
        assertEquals(exception.getMessage(), "Can't parse date. Wrong format or date/time");
    }

    @Test
    public void testWithoutEmptyConstructor(){
        NoEmptyConstructorException exception = assertThrows(NoEmptyConstructorException.class, () ->{
            Task2.loadFromProperties(WithoutEmptyConstructor.class, PATH_TO_PROPERTIES);
        });
        assertEquals(exception.getMessage(), "There is no empty constructor");
    }

    @Test
    public void testWithoutSetter(){
        NoSuchSetterException exception = assertThrows(NoSuchSetterException.class, () ->{
            Task2.loadFromProperties(WithoutSetter.class, PATH_TO_PROPERTIES);
        });
        assertEquals(exception.getMessage(), "There is problem with set value for  timeProperty");
    }

    @Test
    public void testPrivateConstructor(){
        PrivateConstructor actual = Task2.loadFromProperties(PrivateConstructor.class, PATH_TO_PROPERTIES);
        PrivateConstructor expected =
                new PrivateConstructor("value1", 10, Instant.parse("2022-11-29T18:30:00Z"));
        assertEquals(expected, actual);
    }

    @Test
    public void testPrivateSetter(){
        PrivateSetter actual = Task2.loadFromProperties(PrivateSetter.class, PATH_TO_PROPERTIES);
        PrivateSetter expected =
                new PrivateSetter("value1", 10, Instant.parse("2022-11-29T18:30:00Z"));
        assertEquals(expected, actual);
    }

    @Test
    public void testNullParameters(){
        NullPointerException exception = assertThrows(NullPointerException.class, () ->{
            Task2.loadFromProperties(null,null);
        });
        assertEquals(exception.getMessage(), "clz and propertiesPath must be notNull");
    }


}
