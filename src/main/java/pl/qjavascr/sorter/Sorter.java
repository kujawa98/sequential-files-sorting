package pl.qjavascr.sorter;

import pl.qjavascr.model.ReadingTape;
import pl.qjavascr.model.WritingTape;


import java.io.FileNotFoundException;
import java.io.IOException;

public class Sorter {
    public void sort(ReadingTape readingTape, String outputFileName) throws FileNotFoundException {
        //todo distribute
        ReadingTape readingTape1 = new ReadingTape("src/main/resources/readingTape1");
        ReadingTape readingTape2 = new ReadingTape("src/main/resources/readingTape2");
        ReadingTape outputReadingTape = new ReadingTape("src/main/resources/" + outputFileName);

        //todo merge
        //todo loop
        //todo save

    }

    public void distribute(ReadingTape readingTape, WritingTape readingTape1, WritingTape readingTape2) throws IOException {
//        //todo zapisuj, dopóki rosną
//        WritingTape toSave = readingTape1;
//        Record record = readingTape.readSingleRecord();
//        toSave.save(record);
//
//        while (true) {
//            Record newRecord;
//            try {
//                newRecord = readingTape.readSingleRecord();
//            } catch (RuntimeException ex) {
//                break;
//            }
//            if (newRecord.compareTo(record) < 0) {
//                if (toSave == readingTape1) {
//                    toSave = readingTape2;
//                } else {
//                    toSave = readingTape1;
//                }
//            }
//            toSave.save(newRecord);
//            record = newRecord;
//        }

    }


}
