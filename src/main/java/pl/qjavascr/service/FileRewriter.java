package pl.qjavascr.service;

import pl.qjavascr.model.ReadingTape;
import pl.qjavascr.model.Record;
import pl.qjavascr.model.WritingTape;

import java.io.IOException;

public class FileRewriter {
    private static final String OUTPUT = "src/main/resources/output.dat";

    public static void rewriteFile(String from) throws IOException {
        ReadingTape fromTape = new ReadingTape(from);
        WritingTape toTape = new WritingTape(OUTPUT);
        Record record = fromTape.readRecord();
        do {
            toTape.writeRecord(record);
            record = fromTape.readRecord();
        }
        while (!record.data().isEmpty());
        fromTape.close();
        toTape.close();

    }
}
