package pl.qjavascr.sorter;

import java.io.IOException;

import pl.qjavascr.model.ReadingTape;
import pl.qjavascr.model.Record;
import pl.qjavascr.model.WritingTape;

public class Sorter {

    private static final String TEMP_TAPE_1 = "src/main/resources/tape1.dat";
    private static final String TEMP_TAPE_2 = "src/main/resources/tape2.dat";
    private static final String OUTPUT = "src/main/resources/output.dat";
    private static final String DEFAULT = "src/main/resources/default.dat";
    private boolean isNotSorted = true;
    private int fazy = 0;

    public void sort(boolean printAfterPhase) throws IOException {
        while (isNotSorted) {
            //todo distribute
            ReadingTape rdt = new ReadingTape(OUTPUT);
            WritingTape writingTape1 = new WritingTape(TEMP_TAPE_1);
            WritingTape writingTape2 = new WritingTape(TEMP_TAPE_2);
            distribute(rdt, writingTape1, writingTape2);

            //todo merge
            ReadingTape readingTape1 = new ReadingTape(TEMP_TAPE_1);
            ReadingTape readingTape2 = new ReadingTape(TEMP_TAPE_2);
            WritingTape outputTape = new WritingTape(OUTPUT);
            merge(readingTape1, readingTape2, outputTape);
            fazy++;
            if (printAfterPhase) {
                System.out.println("Faza " + fazy);
                System.out.println("Output tape: ");
                printFileContent(new ReadingTape(OUTPUT));
                System.out.println("First tape: ");
                printFileContent(new ReadingTape(TEMP_TAPE_1));
                System.out.println("Second tape: ");
                printFileContent(new ReadingTape(TEMP_TAPE_2));
                System.out.println();
            }
        }
        System.out.println("Fazy " + fazy);

    }

    public void sortDefault() throws IOException {
        ReadingTape readingTape = new ReadingTape(DEFAULT);
        WritingTape writingTape = new WritingTape(OUTPUT);
        Record record = readingTape.readRecord();
        do {
            writingTape.writeRecord(record);
            record = readingTape.readRecord();
        } while (!record.data().isEmpty());
        readingTape.close();
        writingTape.close();
        sort(false);
    }

    public void distribute(ReadingTape readingTape,
                           WritingTape writingTape1,
                           WritingTape writingTape2) throws IOException {
        WritingTape toSave = writingTape1;
        Record record = readingTape.readRecord();
        toSave.writeRecord(record);

        while (true) {
            Record newRecord;

            newRecord = readingTape.readRecord();

            if (newRecord.data().isEmpty()) {
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
        } //todo od najmniejszego do największego
        writingTape1.close();
        writingTape2.close();
        readingTape.close();
    }

    public void merge(ReadingTape readingTape1, ReadingTape readingTape2, WritingTape writingTape) throws IOException {
        Record record1 = readingTape1.readRecord();
        Record record2 = readingTape2.readRecord();
        Record newRecord1;
        Record newRecord2;

        if (record1.data().isEmpty()) { //todo tutaj sprawdzam któryś plik jest pusty, jeśli tak to posortowane
            do {
                writingTape.writeRecord(record2);
                record2 = readingTape2.readRecord();
            }
            while (!record2.data().isEmpty());
            isNotSorted = false;
            readingTape1.close();
            readingTape2.close();
            writingTape.close();
            return;
        } else if (record2.data().isEmpty()) {
            do {
                writingTape.writeRecord(record1);
                record1 = readingTape1.readRecord();
            }
            while (!record1.data().isEmpty());
            isNotSorted = false;
            readingTape1.close();
            readingTape2.close();
            writingTape.close();
            return;
        }

        while (true) {
            if (record1.data().isEmpty()) {
                do {
                    writingTape.writeRecord(record2);
                    record2 = readingTape2.readRecord();
                }
                while (!record2.data().isEmpty());
                break;
            } else if (record2.data().isEmpty()) {
                do {
                    writingTape.writeRecord(record1);
                    record1 = readingTape1.readRecord();
                }
                while (!record1.data().isEmpty());
                break;
            } else if (record1.compareTo(record2) < 0) {
                writingTape.writeRecord(record1);
                newRecord1 = readingTape1.readRecord();
                if (newRecord1.data().isEmpty() || newRecord1.compareTo(record1) < 0) { //koniec serii
                    while (true) {
                        writingTape.writeRecord(record2);
                        newRecord2 = readingTape2.readRecord();
                        if (newRecord2.data().isEmpty() || newRecord2.compareTo(record2) < 0) {
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
                if (newRecord2.data().isEmpty() || newRecord2.compareTo(record2) < 0) { //koniec serii
                    while (true) {
                        writingTape.writeRecord(record1);
                        newRecord1 = readingTape1.readRecord();
                        if (newRecord1.data().isEmpty() || newRecord1.compareTo(record1) < 0) {
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

    private void printFileContent(ReadingTape readingTape) {
        Record record = readingTape.readRecord();
        while (!record.data().isEmpty()) {
            System.out.print(record.data() + " ");
            record = readingTape.readRecord();
        }
        System.out.println();
    }

}
