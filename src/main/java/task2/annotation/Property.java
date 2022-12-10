package task2.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Property {
    String name() default "";
    String format() default ""; //this will be ignore if format() will be not on Instant field

}
