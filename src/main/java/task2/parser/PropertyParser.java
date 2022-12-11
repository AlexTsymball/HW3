package task2.parser;

import task2.annotation.Property;
import task2.exceptions.DateFormatException;
import task2.exceptions.NoSuchPropertyFileException;
import task2.exceptions.WrongNameException;
import task2.exceptions.WrongTypeException;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PropertyParser {

    public static Map<Field, Object> getFieldFromPropertyFile(Field[] fields, Path pathToProperties) {
        Map<Field, Object> fieldsNameValueMap = new HashMap<>();
        try (FileReader fileReader = new FileReader(String.valueOf(pathToProperties))) {
            Properties properties = new Properties();
            properties.load(fileReader);
            Arrays.stream(fields)
                    .forEach(a -> fieldsNameValueMap.put(a, getValue(a, properties)));

        } catch (IOException e) {
            throw new NoSuchPropertyFileException("No file with path " + pathToProperties);
        }
        return fieldsNameValueMap;
    }

    private static Object getValue(Field a, Properties properties) {
        String name = a.getName();
        if (a.isAnnotationPresent(Property.class) && !a.getAnnotation(Property.class).name().equals("")) {
            if (!checkName(a.getAnnotation(Property.class).name())) {
                throw new WrongNameException("Wrong name format \"" +
                        a.getAnnotation(Property.class).name() + "\"");
            }

            name = a.getAnnotation(Property.class).name();

        }
        Set<Object> keySey = properties.keySet();

        if (keySey.contains(name)) {
            Object fieldType = a.getType();
            String fieldValueFromProperty = properties.getProperty(name);
            if (fieldType.equals(String.class)) {
                return fieldValueFromProperty;
            } else if (fieldType.equals(Integer.class)) {
                if (fieldValueFromProperty.equals("null")) {
                    return null;
                }
                try {
                    return convert2IntegerInt(fieldValueFromProperty);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Wrong input type. Can't convert " + fieldValueFromProperty + " to Integer");
                }
            } else if (fieldType.equals(int.class)) {
                try {
                    return convert2IntegerInt(fieldValueFromProperty);
                } catch (NumberFormatException e) {
                    throw new NumberFormatException("Wrong input type. Can't convert " + fieldValueFromProperty + " to int");
                }
            } else if (fieldType.equals(Instant.class)) {
                String format = "";
                if (a.isAnnotationPresent(Property.class)) {
                    format = a.getAnnotation(Property.class).format();
                }
                return convert2Instant(fieldValueFromProperty, format);
            } else {
                throw new WrongTypeException(
                        "Type of fields must be only String, int, Integer or Instant. Not " + fieldType);
            }
        } else {
            throw new NullPointerException("There is no " + name + " field in properties file");
        }

    }

    /**
     * Перевірка заданого імені. Дозволено:
     * 1. дозволено використання літер та чисел;
     * 2. дозволено використання крапок(.). Кожна крапка має знаходитись між літерами або числами.
     * Дві або більше крапки підряд не дозволено;
     * 3. починатись та закінчуватись має або на літеру або на число;
     * 4. дозволено знак нижнього підкреслення(_).Кожен знак має знаходитись між літерами або числами.
     * Два або більше знаки підряд не дозволено;
     *
     * @param name
     * @return boolean
     */
    private static boolean checkName(String name) {
        Pattern nameFormat = Pattern.compile("^[a-zA-Z0-9]+(_[a-zA-Z0-9]+)*(.[a-zA-Z0-9]+(_[a-zA-Z0-9]+)*)*$");
        Matcher matcher = nameFormat.matcher(name);
        return matcher.find();
    }

    /**
     * Якщо дату не задано, а задано число то відрахування йде від 1 січня 1970 року. Наприклад:
     * value - 6:55
     * format - H:ss
     * Instant - 1970-01-01T06:00:55Z
     * Якщо дату та час не задано, то буде 1970-01-01T00:00:00Z.
     * TimeZone вихідного Instant буде UTC. Наприклад:
     * value - Tue 02 Jan 2018 18:07:59 IST
     * format - E dd MMM yyyy HH:mm:ss z
     * Instant - 2018-01-02T16:07:59Z
     *
     * Locale задано UK. Тож якщо дата буде задано не англійською, то буде помилка. Наприклад:
     * value - mardi janvier 2018 14:51:02.354+0530
     * format - EEEEE MMMMM yyyy HH:mm:ss.SSSZ
     * return DataFormatException
     *
     * @param value
     * @param format
     * @return Instant
     */
    private static Instant convert2Instant(String value, String format) {
        Instant instant;
        if (format.equals("")) {
            if (value.equals("")) {
                return Instant.ofEpochSecond(0);
            } else if (value.equals("null")) {
                return null;
            }
            try {
                return Instant.parse(value);
            } catch (DateTimeParseException dateTimeParseException) {
                try {
                    return Instant.ofEpochSecond(Long.parseLong(value));
                } catch (DateTimeParseException | NumberFormatException e) {
                    throw new DateFormatException("Can't parse date. Date format not specified");
                }
            }
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date;
            try {
                date = simpleDateFormat.parse(value);
            } catch (ParseException e) {
                throw new DateFormatException("Can't parse date. Wrong format or date/time");
            }
            instant = date.toInstant();
            return instant;
        } catch (DateTimeParseException | IllegalArgumentException e) {
            throw new DateFormatException("Can't parse date. Wrong format");

        }

    }


    private static Integer convert2IntegerInt(String value) throws NumberFormatException {
        return Integer.parseInt(value);

    }


}
