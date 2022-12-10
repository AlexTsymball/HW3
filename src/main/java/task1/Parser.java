package task1;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import task1.model.Statistics;
import task1.model.TypeAndSum;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;


public class Parser implements Supplier<Statistics> {
    private File file = null;
    private File[] files = null;
    private final Statistics statistics;

    public Parser(File file, Statistics statistics) {
        this.file = file;
        this.statistics = statistics;
    }

    public Parser(File[] files, Statistics statistics) {
        this.files = files;
        this.statistics = statistics;
    }


    private synchronized void getFineTypeAmountFromJson(File inputFile) throws IOException {
        JsonFactory jsonFactory = new JsonFactory();
        try (JsonParser jsonParser = jsonFactory.createParser(inputFile)) {
            TypeAndSum typeAndSum;
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                typeAndSum = new TypeAndSum();
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
                    String fieldname = jsonParser.getCurrentName();
                    if ("type".equals(fieldname)) {
                        jsonParser.nextToken();
                        typeAndSum.setType(jsonParser.getText());
                    } else if ("fine_amount".equals(fieldname)) {
                        jsonParser.nextToken();
                        typeAndSum.setSumFineAmount(jsonParser.getDoubleValue());
                    }
                }
                statistics.addFines(typeAndSum);
            }
        }
    }

    @Override
    public Statistics get() {
        if (files != null) {
            for (File file : files) {
                try {
                    getFineTypeAmountFromJson(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                statistics.groupAndSort();
            }
        } else {
            try {
                getFineTypeAmountFromJson(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            statistics.groupAndSort();
        }
        return statistics;
    }


}
