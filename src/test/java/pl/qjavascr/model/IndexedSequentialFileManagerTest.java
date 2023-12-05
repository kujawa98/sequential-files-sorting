package pl.qjavascr.model;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pl.qjavascr.service.IndexedSequentialFileManager;

public class IndexedSequentialFileManagerTest {

    IndexedSequentialFileManager indexedSequentialFileManager;
    MainDataPagedFile mainDataPagedFile;

    @BeforeEach
    void setUp() throws IOException {
        mainDataPagedFile = new MainDataPagedFile("src/test/resources/writing.dat");
        indexedSequentialFileManager = new IndexedSequentialFileManager(new IndexPagedFile(
                "src/test/resources/index.idx"),
                mainDataPagedFile,
                new MainDataPagedFile(
                        "src/test/resources/overflow.dat"));
    }

    @Test
    void testAddKey() throws IOException {

        var page = mainDataPagedFile.readPage(1);
        indexedSequentialFileManager.addRecord(0, "qwertyuiopqwertyuiopqwertyuiop");
        indexedSequentialFileManager.addRecord(4, "qwertyuiopqwertyuiopqwertyuiop");
        indexedSequentialFileManager.addRecord(5, "qwertyuiopqwertyuiopqwertyuiop");
        indexedSequentialFileManager.addRecord(8, "qwertyuiopqwertyuiopqwertyuiop");
        indexedSequentialFileManager.addRecord(3, "qwertyuiopqwertyuiopqwertyuiop");
        indexedSequentialFileManager.addRecord(9, "qwertyuiopqwertyuiopqwertyuiop");
        indexedSequentialFileManager.addRecord(2, "qwertyuiopqwertyuiopqwertyuiop");
        indexedSequentialFileManager.addRecord(7, "abcdefghijklmnoprstuwyqvxzabcd");
        indexedSequentialFileManager.addRecord(21, "abcdefghijklmnoprstuwyqvxzabcd");
        indexedSequentialFileManager.addRecord(25, "abcdefghijklmnoprstuwyqvxzabcd");
    }

}
