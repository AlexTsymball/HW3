package task2.classesForTest;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import task2.annotation.Property;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class WithoutSetter {


    @Property(format="dd.MM.yyyy HH:mm")
    private Instant timeProperty;


}
