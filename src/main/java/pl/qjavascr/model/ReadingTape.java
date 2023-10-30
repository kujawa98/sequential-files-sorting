package pl.qjavascr.model;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static pl.qjavascr.util.ConstantsUtils.*;

public class ReadingTape {
    private final DataInputStream tape;
    private final String fileName;
    private final byte[] buffer;
    private int bufferReadIndex = 0;
    private int blockDelimiter = BUFFER_SIZE;

    public ReadingTape(String fileName) throws IOException {
        this.fileName = fileName;
        this.tape = new DataInputStream(new FileInputStream(fileName));
        this.buffer = new byte[BUFFER_SIZE];
        this.tape.read(this.buffer);
    }

    public Record readRecord() throws IOException {
        if (bufferReadIndex == blockDelimiter) {
            throw new RuntimeException("Last record");
        }
        byte[] bytes = new byte[RECORD_LEN];
        for (int i = 0; i < RECORD_LEN; i++) {
            bytes[i] = buffer[bufferReadIndex++];
            if (bufferReadIndex == blockDelimiter) {
                if (blockDelimiter < BUFFER_SIZE) {
                    break;
                }
                try {
                    blockDelimiter = readBuffer();
                } catch (RuntimeException ex) {
                    return new Record(new String(bytes).trim());
                } finally {
                    bufferReadIndex = 0;
                }
            }
        }
        return new Record(new String(bytes).trim());
    }

    private int readBuffer() throws IOException {
        int result = tape.read(buffer);
        if (result == -1) {
            throw new RuntimeException("End of file");
        }
        return result;
    }


    public void close() throws IOException {
        tape.close();
    }
}
