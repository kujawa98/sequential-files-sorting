package pl.qjavascr.service;

import pl.qjavascr.model.ReadingTape;
import pl.qjavascr.model.Record;

import java.io.IOException;

public class FilePrinter {
    public static void printFileContent(String path){
        try {
            ReadingTape readingTape = new ReadingTape(path,false);
            Record record = readingTape.readRecord(false);
            while (!record.data().isEmpty()) {
                System.out.print(record.data() + " ");
                record = readingTape.readRecord(false);
            }
            System.out.println();
            readingTape.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
