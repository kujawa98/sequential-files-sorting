package pl.qjavascr.service;

import pl.qjavascr.model.Record;
import pl.qjavascr.model.WritingTape;

import java.io.FileNotFoundException;
import java.util.Scanner;

import static pl.qjavascr.util.ConstantsUtils.RECORD_LEN;

public class FromKeyboardGenerator {
    private static final String OUTPUT = "src/main/resources/output.dat";

    public static void readFromKeyboard(int howManyRecords) throws FileNotFoundException {
        WritingTape toTape = new WritingTape(OUTPUT);
        Scanner input = new Scanner(System.in);
        for (int i = 0; i < howManyRecords; i++) {
            System.out.print("Enter record content: ");
            String record = input.nextLine();
            if (record.length() > RECORD_LEN) {
                System.out.println("Illegal record length - " + record.length() + " instead of " + RECORD_LEN);
                continue;
            }
            toTape.writeRecord(new Record(record));
        }
        toTape.close();
    }
}
