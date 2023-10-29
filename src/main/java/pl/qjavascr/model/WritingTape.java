package pl.qjavascr.model;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class WritingTape {
    private static final int BLOCK_SIZE = 23;
    private static final int RECORD_LEN = 10;
    private final DataOutputStream tape;
    private final String fileName;
    private byte[] buffer;
    private int bufferWriteIndex = 0;

    public WritingTape(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        this.tape = new DataOutputStream(new FileOutputStream(fileName));
        this.buffer = new byte[BLOCK_SIZE];
    }

    public void writeRecord(Record record) {
        byte[] bytes = record.data().getBytes();
        for (int i = 0; i < RECORD_LEN; i++) {
            if (i > bytes.length - 1) {
                buffer[bufferWriteIndex++] = ' ';
                continue;
            }
            buffer[bufferWriteIndex++] = bytes[i];
            if (bufferWriteIndex == BLOCK_SIZE) {
                System.out.println("Full buffer");
                writeBuffer();
                bufferWriteIndex = 0;
                for (int j = i + 1; j < RECORD_LEN; j++) {
                    if (j > bytes.length - 1) {
                        buffer[bufferWriteIndex++] = ' ';
                        continue;
                    }
                    buffer[bufferWriteIndex++] = bytes[j];

                }
                writeBuffer();
                return;
            }
        }
    }

    public void writeBuffer() {
        try {
            tape.write(buffer, 0, bufferWriteIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            tape.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
