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
    private int blockDelimiter = BUFFER_SIZE; //zakładam że rozmiar bufora jest podzielny przez rozmiar bloku

    public ReadingTape(String fileName) throws IOException {
        this.fileName = fileName;
        this.tape = new DataInputStream(new FileInputStream(fileName));
        this.buffer = new byte[BUFFER_SIZE];
        this.tape.read(this.buffer);
    }

    public Record readRecord() throws IOException {
        if (blockDelimiter == -1) { //delimiter równy -1 symbolizuje koniec pliku
            return new Record("");
        }
        byte[] bytes = new byte[RECORD_LEN];
        for (int i = 0; i < RECORD_LEN; i++) {
            bytes[i] = buffer[bufferReadIndex++];
            if (bufferReadIndex == blockDelimiter) {
                if (blockDelimiter < BUFFER_SIZE) {
                    blockDelimiter = -1;
                    break;
                }
                blockDelimiter = tape.read(buffer);
                if (blockDelimiter == -1) {
                    return new Record(new String(bytes).trim());
                } else {
                    bufferReadIndex = 0;
                }
            }
        }
        return new Record(new String(bytes).trim());
    }


    public void close() throws IOException {
        tape.close();
    }
}
