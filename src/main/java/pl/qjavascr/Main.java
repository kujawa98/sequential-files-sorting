package pl.qjavascr;


import pl.qjavascr.model.IndexPagedFile;
import pl.qjavascr.model.MainDataPagedFile;
import pl.qjavascr.service.IndexedSequentialFileManager;

import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        int choice;
        Scanner input = new Scanner(System.in);
        boolean running = true;
        IndexedSequentialFileManager indexedSequentialFileManager = new IndexedSequentialFileManager(new IndexPagedFile(
                "src/test/resources/index.idx"),
                new MainDataPagedFile("src/test/resources/writing.dat"), new MainDataPagedFile(
                "src/test/resources/overflow.dat")
        );
        while (running) {
            System.out.println("  1. Insert key");
            System.out.println("  2. Delete key");
            System.out.println("  3. Reorganize file");
            System.out.println("  4. Update key");
            System.out.println("  5. Read record");
            System.out.println("Choose one:");
            choice = input.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.println("Insert key: ");
                    int key = input.nextInt();
                    System.out.println("Insert data: ");
                    input.nextLine();
                    String data = input.nextLine();
                    indexedSequentialFileManager.addRecord(key, data);
                }
                case 2 -> {
                    System.out.println("Insert key: ");
                    int key = input.nextInt();
                    indexedSequentialFileManager.deleteRecord(key);
                }
                case 3 -> {
                    indexedSequentialFileManager.reorganize();
                }
                case 4 -> {
                    System.out.println("Insert key: ");
                    int key = input.nextInt();
                    System.out.println("Insert data: ");
                    input.nextLine();
                    String data = input.nextLine();
                    indexedSequentialFileManager.updateRecord(key, data);
                }
                case 5 -> {
                    System.out.println("Insert key: ");
                    int key = input.nextInt();
                    indexedSequentialFileManager.readRecord(key);
                }
                case 7 -> {
                    System.out.println("Exit");
                    running = false;
                }
                default -> System.out.println("Uknown value");
            }
        }
        input.close();
        indexedSequentialFileManager.close();
    }
}

//todo wstawienie -> wyszukaj najbardziej sensowny indeks w pliku indeksowym, potem wstaw do obszaru głównego, jeżeli wolne to git, jeżeli nie to bardzo niedobrze