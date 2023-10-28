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
    private final byte[] buffer;
    private int bufferReadIndex = 0;

    public Tape(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        this.tape = new DataInputStream(new FileInputStream(fileName));
        this.buffer = new byte[BLOCK_SIZE];
    }

    public Record readSingleRecord() throws IOException {
        byte[] bytes = new byte[RECORD_LEN];
        for (int i = 0; i < RECORD_LEN; i++) {
            bytes[i] = buffer[bufferReadIndex++];
            if (bufferReadIndex == BLOCK_SIZE) {
                try {
                    readSingleBlock();
                } catch (RuntimeException ex) {
                    return new Record(new String(bytes).trim());
                } finally {
                    bufferReadIndex = 0;
                }
            }
        }
        return new Record(new String(bytes).trim());
    }

    public int readSingleBlock() throws IOException {
        int result = tape.read(buffer);
        if (result == -1) {
            throw new RuntimeException("End of file");
        }
        return result;
    }

    public void close() throws IOException {
        this.tape.close();
    }
}
