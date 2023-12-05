package pl.qjavascr.model;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.qjavascr.service.IndexedSequentialFileManager;

public class IndexedSequentialFileManagerTest {

    IndexedSequentialFileManager indexedSequentialFileManager;

    @BeforeEach
    void setUp() throws IOException {
        indexedSequentialFileManager = new IndexedSequentialFileManager(new IndexPagedFile(
                "src/test/resources/index.idx"), new MainDataPagedFile("src/test/resources/writing.dat"));
    }

    @Test
    void testAddKey() throws IOException {
        indexedSequentialFileManager.addRecord(0,"qwertyuiopqwertyuiopqwertyuiop");
        indexedSequentialFileManager.addRecord(4,"qwertyuiopqwertyuiopqwertyuiop");
        indexedSequentialFileManager.addRecord(5,"qwertyuiopqwertyuiopqwertyuiop");
        indexedSequentialFileManager.addRecord(8,"qwertyuiopqwertyuiopqwertyuiop");
        indexedSequentialFileManager.addRecord(3,"qwertyuiopqwertyuiopqwertyuiop");
        indexedSequentialFileManager.addRecord(7,"abcdefghijklmnoprstuwyqvxzabcd");
    }

}
