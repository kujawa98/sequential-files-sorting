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

    public void merge(ReadingTape readingTape1, ReadingTape readingTape2, WritingTape writingTape) throws IOException {
        Record record1 = readingTape1.readRecord();
        Record record2 = readingTape2.readRecord();
        Record newRecord1;
        Record newRecord2;

        while (true) {
            if (record1.data().isEmpty()) {
                do {
                    writingTape.writeRecord(record2);
                    record2 = readingTape2.readRecord();
                } while (!record2.data().isEmpty());
                break;
            } else if (record2.data().isEmpty()) {
                do {
                    writingTape.writeRecord(record1);
                    record1 = readingTape1.readRecord();
                } while (!record1.data().isEmpty());
                break;
            } else if (record1.compareTo(record2) <= 0) {
                writingTape.writeRecord(record1);
                newRecord1 = readingTape1.readRecord();
                if (newRecord1.data().isEmpty() || newRecord1.compareTo(record1) > 0) { //koniec serii
                    while (true) {
                        writingTape.writeRecord(record2);
                        newRecord2 = readingTape2.readRecord();
                        if (newRecord2.data().isEmpty() || newRecord2.compareTo(record2) > 0) {
                            record2 = newRecord2;
                            break;
                        } else {
                            record2 = newRecord2;
                        }
                    }
                }
                record1 = newRecord1;
            } else {
                writingTape.writeRecord(record2);
                newRecord2 = readingTape2.readRecord();
                if (newRecord2.data().isEmpty() || newRecord2.compareTo(record2) > 0) { //koniec serii
                    while (true) {
                        writingTape.writeRecord(record1);
                        newRecord1 = readingTape1.readRecord();
                        if (newRecord1.data().isEmpty() || newRecord1.compareTo(record1) > 0) {
                            record1 = newRecord1;
                            break;
                        } else {
                            record1 = newRecord1;
                        }
                    }
                }
                record2 = newRecord2;
            }
        }
        readingTape1.close();
        readingTape2.close();
        writingTape.close();
    }

}
