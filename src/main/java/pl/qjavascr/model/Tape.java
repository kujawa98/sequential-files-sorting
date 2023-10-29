package pl.qjavascr.model;

import java.io.*;

public class Tape {
    private static final int BLOCK_SIZE = 23;
    private static final int RECORD_LEN = 10;
    private final DataInputStream tape;
//    private final DataOutputStream dataOutputStream;
    private final String fileName;
    private final byte[] buffer;
    private int bufferReadIndex = 0;
    private int blockDelimiter = BLOCK_SIZE;

    public Tape(String fileName) throws FileNotFoundException {
        this.fileName = fileName;
        this.tape = new DataInputStream(new FileInputStream(fileName));
//        this.dataOutputStream = new DataOutputStream(new FileOutputStream(fileName));
        this.buffer = new byte[BLOCK_SIZE];
    }

    public Record readSingleRecord() throws IOException {
        if (bufferReadIndex == blockDelimiter) {
            throw new RuntimeException("Last record");
        }
        byte[] bytes = new byte[RECORD_LEN];
        for (int i = 0; i < RECORD_LEN; i++) {
            bytes[i] = buffer[bufferReadIndex++];
            if (bufferReadIndex == blockDelimiter) {
                if (blockDelimiter < BLOCK_SIZE) {
                    break;
                }
                try {
                    blockDelimiter = readSingleBlock();
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

    public void save(Record record) throws IOException {
//        dataOutputStream.writeBytes(record.data());
    }


    public void close() throws IOException {
        tape.close();
    }
}
