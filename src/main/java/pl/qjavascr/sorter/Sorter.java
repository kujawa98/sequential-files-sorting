package pl.qjavascr.sorter;

import pl.qjavascr.model.Record;
import pl.qjavascr.model.Tape;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Sorter {
    public void sort(Tape tape, String outputFileName) throws FileNotFoundException {
        //todo distribute
        Tape tape1 = new Tape("src/main/resources/tape1");
        Tape tape2 = new Tape("src/main/resources/tape2");
        Tape outputTape = new Tape("src/main/resources/" + outputFileName);

        //todo merge
        //todo loop
        //todo save

    }

    public void distribute(Tape tape, Tape tape1, Tape tape2) throws IOException {
        //todo zapisuj, dopóki rosną
        Tape toSave = tape1;
        Record record = tape.readSingleRecord();
        toSave.save(record);

        while (true) {
            Record newRecord;
            try {
                newRecord = tape.readSingleRecord();
            } catch (RuntimeException ex) {
                break;
            }
            if (newRecord.compareTo(record) < 0) {
                if (toSave == tape1) {
                    toSave = tape2;
                } else {
                    toSave = tape1;
                }
            }
            toSave.save(newRecord);
            record = newRecord;
        }

    }


}
