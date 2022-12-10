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
import java.util.stream.Collectors;


public class PropertyParser {

    public static Map<Field, Object> getFieldFromPropertyFile(Field[] fields, Path pathToProperties) {
        Map<Field, Object> fieldsNameValueMap;
        try (FileReader fileReader = new FileReader(String.valueOf(pathToProperties))) {
            Properties properties = new Properties();
            properties.load(fileReader);
            fieldsNameValueMap = Arrays.stream(fields)
                    .collect(Collectors.toMap(a -> a,
                            a -> {
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
                                    } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                                        return convert2Integer(fieldValueFromProperty);
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
                    ));
        } catch (IOException e) {
            throw new NoSuchPropertyFileException("No file with path " + pathToProperties);
        }
        return fieldsNameValueMap;
    }

    /**
     * Перевірка заданого імені. Дозволено:
     * 1. дозволено використання літер та чисел;
     * 2. дозволено використання крапок(.). Кожна крапка має знаходитись між літерами або числами.
     *      Дві або більше крапки підряд не дозволено;
     * 3. починатись та закінчуватись має або на літеру або на число;
     * 4. дозволено знак нижнього підкреслення(_).Кожен знак має знаходитись між літерами або числами.
     *      Два або більше знаки підряд не дозволено;
     *
     * @param name
     * @return boolean
     */
    private static boolean checkName(String name) {
//        Pattern nameFormat = Pattern.compile("^\\w+[^.]*(.\\w+[^.]*)*$"); //letters . and other characters
//        Pattern nameFormat = Pattern.compile("^\\w+(.\\w+)*$"); //only letters . _
        Pattern nameFormat = Pattern.compile("^[a-zA-Z0-9]+(_[a-zA-Z0-9]+)*(.[a-zA-Z0-9]+(_[a-zA-Z0-9]+)*)*$"); //only letters, without two or more _
        Matcher matcher = nameFormat.matcher(name);
        return matcher.find();
    }

    /**
     * Якщо дату не задано, то відрахування йде від 1 січня 1970 року.
     * Якщо дату та час не задано, то буде 1970-01-01T00:00:00Z.
     * TimeZone вихідного Instant буде UTC
     * @param value
     * @param format
     * @return Instant
     */
    private static Instant convert2Instant(String value, String format) {
//        value = "Tue 02 Jan 2018 18:07:59 IST";
//        value = "22:55:37-0800";
//        value = "";
//        value = "6:55";
//         value = "Tue 02 Jan 2018";
//         value = "09:15:30 PM, Sun 10/09/2022";
//         value = "5:45:22 AM";
//         value = "0000-01-01T18:30:00Z";
//         format = "";
//         format = "E dd MMM yyyy HH:mm:ss z";
//         format = "HH:mm:ssZZZZ";
//        format = "E dd MMM yyyy";
//         format = "hh:mm:ss a, EEE M/d/uuuu";
//         format = "h:mm:ss a";
//        format = "H:ss";
//        format = "";
//        System.out.println(format);
//        System.out.println(value);
        Instant instant ;
        if (format.equals("")) {
            if (value.equals("")) {
                return Instant.ofEpochSecond(0);
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.UK);
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


    private static Integer convert2Integer(String value) {
        try {
            return Integer.parseInt(value);

        } catch (NumberFormatException numberFormatException) {
            throw new NumberFormatException("Wrong input type. Can't convert " + value + " to Integer");
        }
    }

}
