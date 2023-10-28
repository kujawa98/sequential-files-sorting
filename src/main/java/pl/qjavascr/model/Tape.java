package pl.qjavascr.model;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Tape {
    private static final int BLOCK_SIZE = 24;
    private static final int RECORD_LEN = 10;
    private final DataInputStream tape;
    private final String fileName;

    public Tape(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        this.tape = new DataInputStream(new FileInputStream(fileName));
    }

    public String readSingleRecord() throws IOException {
        byte[] bytes = new byte[RECORD_LEN + 2];
        if (tape.read(bytes) != -1) {
            return new String(bytes).trim();
        }
        return "";
    }

    public String[] readSingleBlock() throws IOException {
        byte[] bytes = new byte[BLOCK_SIZE + 2 * (BLOCK_SIZE / RECORD_LEN)];
        if (tape.read(bytes) != -1) {
            return new String(bytes).trim().split("\r\n");
        }
        return new String[]{};
    }

    public void close() throws IOException {
        this.tape.close();
    }
}
