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

}
