package pl.qjavascr.sorter;

import pl.qjavascr.model.ReadingTape;
import pl.qjavascr.model.Record;
import pl.qjavascr.model.WritingTape;


import java.io.FileNotFoundException;
import java.io.IOException;

public class Sorter {
    public void sort(ReadingTape readingTape, String outputFileName) throws FileNotFoundException {
        //todo distribute
//        ReadingTape readingTape1 = new ReadingTape("src/main/resources/readingTape1");
//        ReadingTape readingTape2 = new ReadingTape("src/main/resources/readingTape2");
//        ReadingTape outputReadingTape = new ReadingTape("src/main/resources/" + outputFileName);

        //todo merge
        //todo loop
        //todo save

    }

    public void distribute(ReadingTape readingTape, WritingTape writingTape1, WritingTape writingTape2) throws IOException {
        WritingTape toSave = writingTape1;
        Record record = readingTape.readRecord();
        toSave.writeRecord(record);

        while (true) {
            Record newRecord;
            try {
                newRecord = readingTape.readRecord();
            } catch (RuntimeException ex) {
                break;
            }
            if (newRecord.compareTo(record) < 0) {
                if (toSave == writingTape1) {
                    toSave = writingTape2;
                } else {
                    toSave = writingTape1;
                }
            }
            toSave.writeRecord(newRecord);
            record = newRecord;
        }
        writingTape1.close();
        writingTape2.close();
        readingTape.close();
    }

    public void merge(ReadingTape readingTape1, ReadingTape readingTape2, WritingTape writingTape) {

    }

}
