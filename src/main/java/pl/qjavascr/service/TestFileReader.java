package pl.qjavascr.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class TestFileReader {
    public static void testFile(IndexedSequentialFileManager fileManager) throws IOException {
        String file = "src/main/resources/test.txt";
        Scanner scanner = new Scanner(new File(file));

        while (scanner.hasNext()) {
            String[] tokens = scanner.nextLine().split(" ");
            switch (tokens[0]) {
                case "I" -> {
                    int key = Integer.parseInt(tokens[1]);
                    String value = tokens[2];
                    fileManager.addRecord(key, value);
                }
                case "D" -> {
                    int key = Integer.parseInt(tokens[1]);
                    fileManager.deleteRecord(key);
                }
                case "R" -> {
                    fileManager.reorganize();
                }
                case "U" -> {
                    int key = Integer.parseInt(tokens[1]);
                    String value = tokens[2];
                    fileManager.updateRecord(key, value);
                }
                default -> {
                    System.out.println("No such option as " + Arrays.toString(tokens));
                }
            }
        }
        scanner.close();
    }
}
