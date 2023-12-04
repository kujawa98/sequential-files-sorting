package pl.qjavascr.model;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class WritingTapeTest {

    @Test
    void testGowno() throws IOException {
        MainDataPagedFile readingTape = new MainDataPagedFile("src/test/resources/writing.dat");
        var page = readingTape.readPage(1);
        page = readingTape.readPage(4);
        System.out.println(page);
    }

    @Test
    void testReadRecord() throws IOException {
        MainDataPagedFile readingTape = new MainDataPagedFile("src/test/resources/writing.dat");
        Record r;
        for (int i = 0; i < 4; i++) {
            r = readingTape.readData(i + 1, 2137 + i);
            System.out.println(r);
        }
    }
}
