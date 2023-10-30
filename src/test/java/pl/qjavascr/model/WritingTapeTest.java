package pl.qjavascr.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

public class WritingTapeTest {
    WritingTape writingTape;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        writingTape = new WritingTape("src/test/resources/writing.txt");
    }

    @Test
    void testWrite() throws IOException {
        var rec = new Record("aaaaaaaaaz");

        writingTape.writeRecord(rec);
        writingTape.writeRecord(rec);
        writingTape.writeRecord(rec);


        writingTape.close();
    }
}
