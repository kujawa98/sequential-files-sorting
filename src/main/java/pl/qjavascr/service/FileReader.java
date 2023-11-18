package pl.qjavascr.service;

import pl.qjavascr.model.Record;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static pl.qjavascr.util.ConstantsUtils.BUFFER_SIZE;
import static pl.qjavascr.util.ConstantsUtils.RECORD_LEN;

public class FileReader {
    private final String INDEX_FILE_NAME = "src/main/resources/index.idx";
    private final String MAIN_AREA_FILE_NAME = "src/main/resources/main.dat";
    private final String OVERFLOW_AREA_FILE_NAME = "src/main/resources/overflow.dat";

    public void readWholeFile() {
        byte[] recordBytes = new byte[BUFFER_SIZE];
        DataInputStream mainArea;
        DataInputStream overflowArea;
        try {
            mainArea = new DataInputStream(new FileInputStream(MAIN_AREA_FILE_NAME));
            overflowArea = new DataInputStream(new FileInputStream(OVERFLOW_AREA_FILE_NAME));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int readBytes = 0;
        while (readBytes != -1) {

        }

    }
}
