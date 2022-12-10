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
        private Lock lock = new ReentrantLock();
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
        System.out.println("!!!!!!!!!!start " + Thread.currentThread().getName() + inputFile.getName());
        long start = System.currentTimeMillis();

        JsonFactory jsonFactory = new JsonFactory();
        System.out.println("@@@start " + Thread.currentThread().getName());

        try (JsonParser jsonParser = jsonFactory.createParser(inputFile)) {
            TypeAndSum typeAndSum;
            System.out.println("1@@@start " + Thread.currentThread().getName());

//            lock.lock();
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                System.out.println("2@@@start " + Thread.currentThread().getName() + " " + System.currentTimeMillis() );

                typeAndSum = new TypeAndSum();
//                StringBuilder object = new StringBuilder();
                while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
//                    System.out.println("3@@@start " + Thread.currentThread().getName());

                    String fieldname = jsonParser.getCurrentName();
                    if ("type".equals(fieldname)) {
//                        System.out.println("4@@@start " + Thread.currentThread().getName() );

                        jsonParser.nextToken();
                        typeAndSum.setType(jsonParser.getText());
//                        System.out.println("41@@@start " + Thread.currentThread().getName() );

                    } else if ("fine_amount".equals(fieldname)) {
//                        System.out.println("@5@@start " + Thread.currentThread().getName() );

                        jsonParser.nextToken();
                        typeAndSum.setSumFineAmount(jsonParser.getDoubleValue());
//                        System.out.println("@51@@start " + Thread.currentThread().getName() );

                    }
//                    System.out.println("3@@@end " + Thread.currentThread().getName());

                }
//                System.out.println("before add " + Thread.currentThread().getName() );

                statistics.addFines(typeAndSum);
//                System.out.println("after add " + Thread.currentThread().getName() );
                System.out.println("2@@@end " + Thread.currentThread().getName()+ " " + System.currentTimeMillis() );

            }
//            lock.unlock();

        }
        System.out.println("!!!!!!!!!end " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start) +
                "\t" + file.length() );

    }
    //work slowly, better result
    private void getFineTypeAmountFromJson1(File inputFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(Thread.currentThread().getName());

        System.out.println("!!!!!!!!!!start " + Thread.currentThread().getName() + inputFile.getName());
        try (Scanner scanner = new Scanner(inputFile).useDelimiter("},")) {
            System.out.println("@@@start " + Thread.currentThread().getName());

            while (scanner.hasNext()) {
//                System.out.println("1@@@start " + Thread.currentThread().getName());
                String row = scanner.next();
                row = row + "}";
                row = row.replaceAll("[\\[\\]]", "");
//                lock.lock();
                TypeAndSum typeAndSum = mapper.readValue(row, TypeAndSum.class);
//                TypeAndSum[] typeAndSum = mapper.readValue(inputFile, TypeAndSum[].class);
//                 lock.unlock();
                statistics.addFines(typeAndSum);
//                statistics.addAllFines(typeAndSum);
//                System.out.println("1@@@end " + Thread.currentThread().getName());

            }
            System.out.println("@@@end " + Thread.currentThread().getName());

        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
//        System.out.println(statistics);
        System.out.println("!!!!!!!!!!end " + Thread.currentThread().getName() + inputFile.getName());

    }

    @Override
    public Statistics get() {
        System.out.println("start " + Thread.currentThread().getName());
        long start = System.currentTimeMillis();
//        List<Integer> count = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                System.out.println("loop start " + Thread.currentThread().getName());
                long start1 = System.currentTimeMillis();
                try {
                    getFineTypeAmountFromJson(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                statistics.groupAndSort();
                System.out.println("loop end " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start1) +
                        "\t" + file.length());

            }
        } else {
            System.out.println("file start " + Thread.currentThread().getName());
            long start2 = System.currentTimeMillis();
            try {
                getFineTypeAmountFromJson(file);
            } catch (IOException e) {
                e.printStackTrace();
            }


            statistics.groupAndSort();
            System.out.println("file end " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start2) +
                    "\t" + file.length());

        }
//        statistics.groupAndSort();
//        System.out.println(Thread.currentThread().getName() + "   " + count);
//        System.out.println("end " + Thread.currentThread().getName());
        System.out.println("end " + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start));

        return statistics;
    }


}
