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
