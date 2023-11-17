package pl.qjavascr.model;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class ReadingTapeTest {
    @Test
    void chuj() throws IOException {
        ReadingTape readingTape = new ReadingTape("src/main/resources/output.dat");
        var r = readingTape.readRecord();
        System.out.println(r);
    }
}
