package pl.qjavascr.model;

import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static pl.qjavascr.util.ConstantsUtils.BUFFER_SIZE;
import static pl.qjavascr.util.ConstantsUtils.RECORD_LEN;

public class WritingTape {

    private final DataOutputStream tape;
    private final byte[]           buffer;
    private       int              bufferWriteIndex = 0;

    public WritingTape(String fileName) throws FileNotFoundException {
        this.tape = new DataOutputStream(new FileOutputStream(fileName));
        this.buffer = new byte[BUFFER_SIZE];
    }

    public void writeRecord(Record record) {
        if (record.data().isEmpty()) {
            return;
        }
        byte[] bytes = record.data().getBytes();
        for (int i = 0; i < RECORD_LEN; i++) {
            if (i > bytes.length - 1) {
                buffer[bufferWriteIndex++] = ' ';
                if (bufferWriteIndex == BUFFER_SIZE) {
                    writeBuffer();
                    bufferWriteIndex = 0;
                    for (int j = i + 1; j < RECORD_LEN; j++) {
                        buffer[bufferWriteIndex++] = ' ';
                    }
                    writeBuffer();
                    bufferWriteIndex = 0;
                }
                continue;
            }
            buffer[bufferWriteIndex++] = bytes[i];
            if (bufferWriteIndex == BUFFER_SIZE) {
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
                bufferWriteIndex = 0;
                return;
            }
        }
    }

    private void writeBuffer() {
        try {
            tape.write(buffer, 0, bufferWriteIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            writeBuffer();
            tape.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
