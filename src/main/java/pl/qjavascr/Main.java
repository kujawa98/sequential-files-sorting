package pl.qjavascr;


import pl.qjavascr.model.IndexPagedFile;
import pl.qjavascr.model.MainDataPagedFile;
import pl.qjavascr.service.IndexedSequentialFileManager;

import java.io.IOException;
import java.util.Scanner;

import static pl.qjavascr.util.ConstantsUtils.reads;
import static pl.qjavascr.util.ConstantsUtils.writes;


public class Main {
    public static void main(String[] args) throws IOException {
        int choice;
        Scanner input = new Scanner(System.in);
        boolean running = true;
        IndexPagedFile indexPagedFile = new IndexPagedFile("src/main/resources/index.idx");
        MainDataPagedFile mainPagedFile = new MainDataPagedFile("src/main/resources/main.dat");
        MainDataPagedFile overflowPagedFile = new MainDataPagedFile("src/main/resources/overflow.dat");
        IndexedSequentialFileManager manager = new IndexedSequentialFileManager(indexPagedFile, mainPagedFile, overflowPagedFile);
        while (running) {
            System.out.println("  1. Insert key");
            System.out.println("  2. Delete key");
            System.out.println("  3. Reorganize file");
            System.out.println("  4. Update key");
            System.out.println("  5. Print index file");
            System.out.println("  6. Print data file");
            System.out.println("  7. Quit");
            System.out.println("Choose one:");
            choice = input.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.println("Key: ");
                    int key = input.nextInt();
                    System.out.println("Value: ");
                    input.nextLine();
                    String value = input.nextLine();
                    manager.addRecord(key, value);
                }
                case 2 -> {
                    System.out.print("Key: ");
                    int key = input.nextInt();
                    manager.deleteRecord(key);
                }
                case 3 -> {
                    System.out.println("Reorganizing");
                    manager.reorganize();
                }
                case 4 -> {
                    System.out.print("Key: ");
                    int key = input.nextInt();
                    System.out.println();
                    System.out.print("Value: ");
                    String value = input.nextLine();
                    manager.updateRecord(key, value);
                }
                case 5 -> {
                    System.out.println("Index file");
                    manager.readIndexFile();
                }
                case 6 -> {
                    System.out.println("Main file");
                    manager.readDataFile(true);
                }
                case 7 -> {
                    System.out.println("Exit");
                    running = false;
                }
                default -> System.out.println("Uknown value");
            }
            System.out.println("Reads: " + reads);
            System.out.println("Writes: " + writes);
        }
        input.close();
        indexPagedFile.close();
    }
}

//todo wstawienie -> wyszukaj najbardziej sensowny indeks w pliku indeksowym, potem wstaw do obszaru głównego, jeżeli wolne to git, jeżeli nie to bardzo niedobrze