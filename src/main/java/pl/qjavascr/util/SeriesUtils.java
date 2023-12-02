package pl.qjavascr.util;

import pl.qjavascr.model.ReadingTape;
import pl.qjavascr.model.Record;

import java.io.IOException;

public class SeriesUtils {
    public static int howManySeries(String fileName) throws IOException {
        int series = 0;
        ReadingTape tape = new ReadingTape(fileName, false);
        Record record = tape.readRecord(false);
        if (record.data().isEmpty()) {
            return series;
        }
        series++;
        Record nextRecord = record;
        while (!nextRecord.data().isEmpty()) {
            nextRecord = tape.readRecord(false);
            if (nextRecord.data().isEmpty()) {
                return series;
            }
            if (nextRecord.compareTo(record) < 0) {
                series++;
            }
            record = nextRecord;
        }
        return series;
    }
}
