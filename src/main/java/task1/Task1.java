package task1;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import task1.model.Statistics;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class Task1 {
//                                  11      22[]     22    22[]ch    22ch
//            1 52665    1 47663 1 35627 1 91794 1 92001 1 230367 1 226647
//            2 49974 55 2 33730 2 32754 2 74826 2 89607 2 172160 2 122593
//            4 48613 49 4 33140 4 28051 4 75775 4 78363 4 111391 4 100310
//            8 43687    8 20765 8 26606 8 72401 8 72349 8 97163  8 96518
    private static final String PATH_TO_DIRECTORIES_WITH_FILES = "src/main/files/";
    //    public static final int NUMBER_OF_THREADS = 8; //1,2,4,8
    public static final int[] THREADS = { 2, 4, 8};
//    public static final int[] THREADS = {2};
    public static final int batchSize = 10;//максимальна кількість файлів в одному потоці за один раз
    private static final int nCPU = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) {
        Task1 task1 = new Task1();
        System.out.println(nCPU);
        for (int thread : THREADS) {
            long start = System.currentTimeMillis();
            long start1 = Calendar.getInstance().getTimeInMillis();
            task1.getStatisticsFromJson2Xml(thread);
            System.out.println(thread + " " + (System.currentTimeMillis() - start));
            System.out.println(thread + " " + (Calendar.getInstance().getTimeInMillis() - (start1)));
        }

    }


    private void getStatisticsFromJson2Xml(int NUMBER_OF_THREADS) {
        File[] listInputFiles = filesWithJson(new File(PATH_TO_DIRECTORIES_WITH_FILES + "input/task1"), "json");
        if (NUMBER_OF_THREADS != 0 && listInputFiles != null) {
            ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

            List<CompletableFuture<Statistics>> arrayThread = new ArrayList<>();


            if (NUMBER_OF_THREADS == 1) {
                arrayThread.add(CompletableFuture.supplyAsync(
                        new Parser(listInputFiles, new Statistics()), executorService));
            } else {
//                List<File[]> splitFilesArray = splitFilesArray(listInputFiles, NUMBER_OF_THREADS);
//
//                for (File[] files : splitFilesArray) {
//                    arrayThread.add(CompletableFuture.supplyAsync(
//                            new Parser(files, new Statistics()), executorService));
//                }
                for (File files : listInputFiles) {
                    arrayThread.add(CompletableFuture.supplyAsync(
                            new Parser(files, new Statistics()), executorService));
                }
            }


            CompletableFuture<Statistics> completableFutureFinish =
                    CompletableFuture.allOf(arrayThread.toArray(new CompletableFuture[0]))
                            .thenApply(t -> arrayThread.stream()
                                    .map(CompletableFuture::join)
                                    .collect(Statistics::new, Statistics::addAllFinesFromStatistics, Statistics::addAllFinesFromStatistics));

//                            .thenAccept(a -> {
//                                System.out.println(Thread.currentThread().getName());
//                                a.groupAndSort();
//                                writeStatistics2XML(a);
//                            });
            Statistics statistics = null;
            try {
                System.out.println("before");
                statistics = completableFutureFinish.get();

                System.out.println("after");

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            executorService.shutdown();
//
            statistics.groupAndSort();
            writeStatistics2XML(statistics);
        }


    }


    /**
     * метод ділить масив з усіма файлами на декілька масивів для паралельного викнання. Величина кодного масива:
     * numberOfFileInOneArray = Math.max(Math.min(listInputFiles.length / (1.25 * numberOfThreads), batchSize), 1);
     * це зроблено для того, щоб у випадку коли файли мають різну довжину не було такого, що один потім виконує роботу 10 секунд,
     * а інші по 50 секунд. Тобто більш рівномірно відбувається розподіл файлів по потоках. Якщо один потім завершив роботу раніше,
     * бо передані йому файли були менші за розміром, то він бере наступний масив файлів.
     *
     * @param listInputFiles
     * @param numberOfThreads
     */
    private List<File[]> splitFilesArray(File[] listInputFiles, int numberOfThreads) {
        List<File[]> splitList = new ArrayList<>();
//        if (numberOfThreads == 1) {
//            splitList.add(listInputFiles);
//            return splitList;
//        }
        int numberOfFileInOneArray = (int) Math.max(
                Math.min((listInputFiles.length / (1.25 * numberOfThreads)), batchSize), 1);//
//        int numberOfFileInOneArray = 1;
        int numberArrays = (listInputFiles.length / (numberOfFileInOneArray));
        int remainder = listInputFiles.length % (numberOfFileInOneArray * numberArrays);
        int start = 0;
        int end = 0;
        System.out.println(listInputFiles.length);
        System.out.println(batchSize);
        System.out.println("number of files in one array " + numberOfFileInOneArray);
        System.out.println(remainder);
        System.out.println(numberArrays);

        for (int i = 0; i < numberArrays; i++) {
            end += numberOfFileInOneArray;
            splitList.add(Arrays.copyOfRange(listInputFiles, start, end));
            start = end;
        }

        if (remainder > 0) {
            splitList.add(Arrays.copyOfRange(listInputFiles, start, (start + remainder)));
        }
        return splitList;
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
            xmlMapper.writeValue(new File(PATH_TO_DIRECTORIES_WITH_FILES + "output/statisticsTask1TEST.xml"),
                    statistics);
//            throw new IOException();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
