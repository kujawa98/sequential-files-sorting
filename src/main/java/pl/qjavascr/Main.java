package pl.qjavascr;


import java.io.IOException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws IOException {
        int choice;
        Scanner input = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("  1. Insert key");
            System.out.println("  2. Delete key");
            System.out.println("  3. Reorganize file");
            System.out.println("  4. Update key");
            System.out.println("  5. Quit");
            System.out.println("Choose one:");
            choice = input.nextInt();

            switch (choice) {
                case 1 -> {

                }
                case 2 -> {

                }
                case 3 -> {
                }
                case 4 -> {

                }
                case 5 -> {
                    System.out.println("Exit");
                    running = false;
                }
                default -> System.out.println("Uknown value");
            }
        }
        input.close();

    }
}

//todo wstawienie -> wyszukaj najbardziej sensowny indeks w pliku indeksowym, potem wstaw do obszaru głównego, jeżeli wolne to git, jeżeli nie to bardzo niedobrze