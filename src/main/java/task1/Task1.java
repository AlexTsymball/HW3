package task1;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import task1.model.Statistics;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Task1 {
    /**
     * Якщо на вхід подається 84 файли по 8мб, то час виконання буде наступний:
     * 1 потоки - 5872 мс
     * 2 потоки - 2631 мс
     * 4 потоки - 1716 мс
     * 8 потоки - 1641 мс
     * Якщо на вхід подається 42 файли по 8мб, то час виконання буде наступний:
     * 1 потоки - 3599 мс
     * 2 потоки - 1365  мс
     * 4 потоки - 847  мс
     * 8 потоки - 755  мс
     * Якщо на вхід подається 11 файлів по 206мб (1,92ГБ данних), то час виконання буде наступний:
     * 1 потоки - 35236 мс
     * 2 потоки - 32763 мс
     * 4 потоки - 24487 мс
     * 8 потоки - 14898 мс
     * 
     *
     */
    private static final String PATH_TO_DIRECTORIES_WITH_FILES = "src/main/files/";
    public static final int[] THREADS = { 1,2,4,8};

    public static void main(String[] args) {
        Task1 task1 = new Task1();
        for (int thread : THREADS) {
            long start = System.currentTimeMillis();
            task1.getStatisticsFromJson2Xml(thread);
            System.out.println(thread + " " + (System.currentTimeMillis() - start));
        }

    }


    private void getStatisticsFromJson2Xml(int numberOfThreads) {
        File[] listInputFiles = filesWithJson(new File(PATH_TO_DIRECTORIES_WITH_FILES + "input/task1"), "json");
        if (numberOfThreads != 0 && listInputFiles != null) {
            ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

            List<CompletableFuture<Statistics>> futureArrayList = new ArrayList<>();
            if (numberOfThreads == 1) {
                futureArrayList.add(CompletableFuture.supplyAsync(
                        new Parser(listInputFiles, new Statistics()), executorService));
            } else {
                for (File files : listInputFiles) {
                    futureArrayList.add(CompletableFuture.supplyAsync(
                            new Parser(files, new Statistics()), executorService));
                }
            }

            CompletableFuture<Void> completableFutureFinish =
                    CompletableFuture.allOf(futureArrayList.toArray(new CompletableFuture[0]))
                            .thenApply(t -> futureArrayList.stream()
                                    .map(CompletableFuture::join)
                                    .collect(Statistics::new, Statistics::addAllFinesFromStatistics, Statistics::addAllFinesFromStatistics)
                            )
                            .thenAccept(a -> {
                                a.groupAndSort();
                                writeStatistics2XML(a);
                            });
            try {
                completableFutureFinish.get();

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
        }
    }


    public File[] filesWithJson(File dir, String json) {
        String end = "." + json;
        return dir.listFiles(
                file -> !file.isDirectory() &&
                        file.getName().endsWith(end) && file.length() != 0
        );
    }


    private void writeStatistics2XML(Statistics statistics) {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            xmlMapper.writeValue(new File(PATH_TO_DIRECTORIES_WITH_FILES + "output/statisticsTask1.xml"),
                    statistics);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
