package pl.qjavascr.service;

import pl.qjavascr.model.ReadingTape;
import pl.qjavascr.model.Record;

import java.io.IOException;

public class FilePrinter {
    public static void printFileContent(String path){
        try {
            ReadingTape readingTape = new ReadingTape(path);
            Record record = readingTape.readRecord();
            while (!record.data().isEmpty()) {
                System.out.print(record.data() + " ");
                record = readingTape.readRecord();
            }
            System.out.println();
            readingTape.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
