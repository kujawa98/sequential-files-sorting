package pl.qjavascr.service;

import org.apache.commons.lang3.RandomStringUtils;
import pl.qjavascr.model.Record;
import pl.qjavascr.model.WritingTape;

import java.io.FileNotFoundException;
import java.util.Random;

public class RandomGenerator {
    private static final Random RANDOM = new Random();
    private static final int MAX_RECORD_LEN = 30;
    private static final String OUTPUT = "src/main/resources/output.dat";

    public static void randomTape(int howManyRecord) {
        WritingTape writingTape = null;
        try {
            writingTape = new WritingTape(OUTPUT);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < howManyRecord; i++) {
            StringBuilder tapeString = new StringBuilder();
            int howManyCharacters = RANDOM.nextInt(MAX_RECORD_LEN) + 1;
            tapeString.append(RandomStringUtils.randomAlphabetic(howManyCharacters).toLowerCase());
            tapeString.append(" ".repeat(MAX_RECORD_LEN - howManyCharacters));
            String toWrite = tapeString.toString();
            writingTape.writeRecord(new Record(toWrite));
        }
        writingTape.close();
    }
}
