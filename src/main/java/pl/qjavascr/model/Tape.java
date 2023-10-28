package pl.qjavascr.model;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class Tape {
    private static final int BLOCK_SIZE = 20;
    private static final int RECORD_LEN = 10;
    private final String fileName;

    public Tape(String fileName) {
        this.fileName = fileName;
    }

    public String readSingleRecord(int offset) throws IOException {
        DataInputStream tape = new DataInputStream(new FileInputStream(fileName));
        tape.skipBytes((RECORD_LEN + 2) * offset);
        byte[] bytes = new byte[RECORD_LEN];
        tape.read(bytes);
        tape.close();
        String s = new String(bytes).trim();
        return s;
    }

    public String[] readSingleBlock() throws IOException {
        DataInputStream tape = new DataInputStream(new FileInputStream(fileName));
        byte[] bytes = new byte[BLOCK_SIZE + 2];
        tape.read(bytes);
        tape.close();
        String[] s = new String(bytes).trim().split("\r\n");
        return s;
    }
}
