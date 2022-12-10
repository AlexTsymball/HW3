package task1.serializer;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.DecimalFormat;

public class CustomDoubleSerializer extends JsonSerializer {
    @Override
    public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        if (null == value) {
            //write the word 'null' if there's no value available
            jgen.writeNull();
        } else {
            final String pattern = ".##";
//            final String pattern = "#,###.00";
            final DecimalFormat myFormatter = new DecimalFormat(pattern);
            final String output = myFormatter.format(value);
//            System.out.println(output);
//            System.out.println("serializer " + Thread.currentThread().getName());

            jgen.writeNumber(output);
        }
    }

}
