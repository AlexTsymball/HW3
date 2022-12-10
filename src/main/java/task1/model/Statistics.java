package task1.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@JsonAutoDetect
@JacksonXmlRootElement(localName = "statistics")
public class Statistics{
    @JacksonXmlElementWrapper( useWrapping = false)
    @JacksonXmlProperty(localName = "fine")
    private List<TypeAndSum> fines = new ArrayList<>();


    public  void addFines(TypeAndSum fine) {
        fines.add(fine);
    }
    public  void addAllFinesFromStatistics(Statistics statistics) {
        fines.addAll(statistics.getFines());
    }

    public List<TypeAndSum> getFines() {
        return fines;
    }


    public  void groupAndSort() {
        fines = fines.stream()
                .collect(Collectors.groupingBy(TypeAndSum::getType, Collectors.summingDouble(TypeAndSum::getSumFineAmount)))
                .entrySet()
                .stream()
                .map(a -> {
                            if (a.getValue() < Double.MAX_VALUE) {
                                return new TypeAndSum(a.getKey(), (a.getValue()));
                            } else {
                                throw new IllegalArgumentException("too big value for right calculate sum");
                            }
                        }
                )
                .sorted(Comparator.comparing(TypeAndSum::getSumFineAmount).reversed())
                .collect(Collectors.toList());
    }


}
