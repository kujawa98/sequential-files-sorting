package pl.qjavascr.sorter;

import pl.qjavascr.model.ReadingTape;
import pl.qjavascr.model.Record;
import pl.qjavascr.model.WritingTape;


import java.io.FileNotFoundException;
import java.io.IOException;

public class Sorter {
    private static final String TEMP_TAPE_1 = "src/main/resources/tape1.txt";
    private static final String TEMP_TAPE_2 = "src/main/resources/tape2.txt";
    private static final String OUTPUT = "src/main/resources/output.txt";
    private boolean isNotSorted = true;

//    public void sort(ReadingTape readingTape) throws IOException {
//        WritingTape writingTape = new WritingTape(OUTPUT);
//
//        //todo rewrite source to output
//        Record record;
//        do {
//            record = readingTape.readRecord();
//            writingTape.writeRecord(record);
//        } while (!record.data().isEmpty());
//        writingTape.close();
//        readingTape.close();
//
//        do {
//            //todo distribute
//            ReadingTape rdt = new ReadingTape(OUTPUT);
//            WritingTape writingTape1 = new WritingTape(TEMP_TAPE_1);
//            WritingTape writingTape2 = new WritingTape(TEMP_TAPE_2);
//            distribute(rdt, writingTape1, writingTape2);
//
//            //todo merge
//            ReadingTape readingTape1 = new ReadingTape(TEMP_TAPE_1);
//            ReadingTape readingTape2 = new ReadingTape(TEMP_TAPE_2);
//            WritingTape outputTape = new WritingTape(OUTPUT);
//            merge(readingTape1, readingTape2, outputTape);
//        } while (!checkIfOutputSorted());
//
//    }

    public void sort() throws IOException {
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
        }

    }

//    private boolean checkIfOutputSorted() throws IOException {
//        ReadingTape readingTape = new ReadingTape(OUTPUT);
//        Record record = readingTape.readRecord();
//        Record nextRecord = readingTape.readRecord();
//        while (!nextRecord.data().isEmpty()) {
//            if (nextRecord.compareTo(record) < 0) {
//                readingTape.close();
//                return false;
//            }
//            record = nextRecord;
//            nextRecord = readingTape.readRecord();
//        }
//        readingTape.close();
//        return true;
//
//    }

    public void distribute(ReadingTape readingTape, WritingTape writingTape1, WritingTape writingTape2) throws IOException {
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
            } while (!record2.data().isEmpty());
            isNotSorted = false;
            readingTape1.close();
            readingTape2.close();
            writingTape.close();
            return;
        } else if (record2.data().isEmpty()) {
            do {
                writingTape.writeRecord(record1);
                record1 = readingTape1.readRecord();
            } while (!record1.data().isEmpty());
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
                } while (!record2.data().isEmpty());
                break;
            } else if (record2.data().isEmpty()) {
                do {
                    writingTape.writeRecord(record1);
                    record1 = readingTape1.readRecord();
                } while (!record1.data().isEmpty());
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

}
