package pl.qjavascr;

import pl.qjavascr.service.FilePrinter;
import pl.qjavascr.service.FileRewriter;
import pl.qjavascr.service.FromKeyboardGenerator;
import pl.qjavascr.service.RandomGenerator;
import pl.qjavascr.sorter.Sorter;

import java.io.IOException;
import java.util.Scanner;

import static pl.qjavascr.util.ConstantsUtils.reads;
import static pl.qjavascr.util.ConstantsUtils.writes;

public class Main {
    private static final Sorter sorter = new Sorter();

    public static void main(String[] args) throws IOException {
        int choice;
        Scanner input = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("  1. Generate random records");
            System.out.println("  2. Read record from keyboard");
            System.out.println("  3. Import from file");
            System.out.println("  4. Print records file");
            System.out.println("  5. Sort");
            System.out.println("  6. Sort with  print after each phase");
            System.out.println("  7. Quit");
            System.out.println("Choose one:");
            choice = input.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.println("How many records?");
                    RandomGenerator.randomTape(input.nextInt());
                }
                case 2 -> {
                    System.out.println("How many records?");
                    FromKeyboardGenerator.readFromKeyboard(input.nextInt());
                }
                case 3 -> {
                    System.out.println("Which file? File should be in src/main/resources/ directory");
                    input.nextLine();
                    String path = input.nextLine();
                    FileRewriter.rewriteFile(path);
                }
                case 4 -> {
                    System.out.println("Which file? File should be in src/main/resources/ directory");
                    input.nextLine();
                    String path = input.nextLine();
                    FilePrinter.printFileContent("src/main/resources/" + path);
                }
                case 5 -> {
                    System.out.println("Sorting");
                    sorter.sort(false);
                }
                case 6 -> {
                    System.out.println("Sorting with printing after every phase");
                    sorter.sort(true);
                }
                case 7 -> {
                    System.out.println("Exit");
                    running = false;
                }
                default -> System.out.println("Uknown value");
            }
            writes = 0;
            reads = 0;
        }
        input.close();

    }
}