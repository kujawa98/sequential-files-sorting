package pl.qjavascr.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

public class MainDataPagedFileTest {
    MainDataPagedFile mainDataPagedFile;

    @BeforeEach
    void setUp() throws IOException {
        mainDataPagedFile = new MainDataPagedFile("src/test/resources/writing.dat");
    }

    @Test
    void testReadBuffer() throws IOException {
        var p1 = mainDataPagedFile.readPage(1);
        var p2 = mainDataPagedFile.readPage(1);
    }

    @Test
    void testWriteBuffer() throws IOException {
        var ar = new ArrayList<Record>();
        ar.add(Record.builder().key(21).data("qwertyuiopasdfghjklzxcvbnmqwer").build());
        ar.add(Record.builder().key(22).data("qwertyuiopasdfghjklzxcvbnmqwer").build());
        ar.add(Record.builder().key(23).data("qwertyuiopasdfghjklzxcvbnmqwer").build());
        ar.add(Record.builder().key(24).data("qwertyuiopasdfghjklzxcvbnmqwer").build());
        var p1 = new Page<>(1, ar);
        var p2 = new Page<>(2, ar);
        mainDataPagedFile.writePage(p1);
        mainDataPagedFile.writePage(p1);
        mainDataPagedFile.close();
    }
}
