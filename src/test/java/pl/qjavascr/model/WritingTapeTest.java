package pl.qjavascr.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static pl.qjavascr.util.ConstantsUtils.BUFFER_SIZE;
import static pl.qjavascr.util.ConstantsUtils.PAGE_SIZE;

public class WritingTapeTest {
    WritingTape writingTape;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        writingTape = new WritingTape("src/test/resources/writing.txt");
    }

//    @Test
//    void testWrite() throws IOException {
//        var rec = new Record("aaaaaaaaaz");
//
//        writingTape.writeRecord(rec);
//        writingTape.writeRecord(rec);
//        writingTape.writeRecord(rec);
//
//
//        writingTape.close();
//    }

    @Test
    void testGowno() throws IOException {
        ReadingTape readingTape = new ReadingTape("src/test/resources/writing.dat");
        var page = readingTape.readPage(1);
        page = readingTape.readPage(4);
        System.out.println(page);
    }
}
