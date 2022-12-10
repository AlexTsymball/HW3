package task2;

import task2.exceptions.NoEmptyConstructorException;
import task2.exceptions.NoSuchSetterException;
import task2.parser.PropertyParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Map;

public class Task2 {
    static final Path PATH_TO_PROPERTIES = Path.of("src/main/resources/task2.properties");

    public static void main(String[] args) {
        Test s = loadFromProperties(Test.class, PATH_TO_PROPERTIES);
        System.out.println(s);
    }

    public static <T> T loadFromProperties(Class<T> cls, Path propertiesPath) {
        if(cls == null || propertiesPath == null){
            throw new NullPointerException("clz and propertiesPath must be notNull");
        }
        Field[] declaredFields = cls.getDeclaredFields();

        Map<Field, Object> fieldNameValue = PropertyParser.getFieldFromPropertyFile(declaredFields, propertiesPath);
//        System.out.println(fieldNameValue);
        Constructor<T> emptyConstructor;
        T classObject;
        try {
            emptyConstructor = cls.getDeclaredConstructor();
            emptyConstructor.setAccessible(true);
            classObject = emptyConstructor.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
//            e.printStackTrace();
            throw new NoEmptyConstructorException("There is no empty constructor");
        }


        fieldNameValue.forEach((key, value) -> {
            try {
                Method setter = cls.getDeclaredMethod("set" + key.getName().replaceFirst(
                        key.getName().substring(0,1), key.getName().substring(0,1).toUpperCase()
                ), key.getType());
                setter.setAccessible(true);
                setter.invoke(classObject, value);
//                PropertyUtils.setProperty(classObject, key.getName(), value);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//                e.printStackTrace();
                throw new NoSuchSetterException("There is problem with set value for  " + key.getName());
            }

        });
        return classObject;

    }

}