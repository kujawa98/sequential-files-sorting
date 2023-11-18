package pl.qjavascr.model;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static pl.qjavascr.util.ConstantsUtils.*;

public class ReadingTape {
    private final RandomAccessFile tape;
    private final byte[] buffer;
    private final int[] pagesInBuffer;
    private int bufferReadIndex = 0;
    private int blockDelimiter = BUFFER_SIZE; //zakładam że rozmiar bufora jest podzielny przez rozmiar bloku

    public ReadingTape(String fileName) throws IOException {
        this.tape = new RandomAccessFile(fileName, "r");
        this.buffer = new byte[BUFFER_SIZE];
        this.pagesInBuffer = new int[BUFFER_SIZE / PAGE_SIZE];
        this.tape.read(this.buffer);
    }

    public Record readRecord() throws IOException {
        if (blockDelimiter == -1) { //delimiter równy -1 symbolizuje koniec pliku
            return null;
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
                    return null;
                } else {
                    bufferReadIndex = 0;
                }
            }
        }
        return null;
    }

    public Page readPage(int pageNumber) throws IOException {
        for (int i = 0; i < buffer.length; i += PAGE_SIZE) {
            int currentPageNumber = buffer[i];
            if (currentPageNumber == pageNumber) {
                List<Record> records = new ArrayList<>();
                for (int j = 0; j < RECORDS_PER_PAGE; j++) {
                    records.add(transformBytesToRecord(i + 1));
                }
            }
        }
        return new Page();
    }

    private Record transformBytesToRecord(int position) {
        int key = (buffer[position] << 24) | (buffer[position + 1] << 16) | (buffer[position + 2] << 8) | (buffer[position + 3]);
        position += 4;
        String data = "";
        for (int i = 0; i < 30; i++) {
//            data += byteDoStringa
        }
        position += 30;
        byte overflowRecordPage = buffer[position++];
        byte overflowRecordPosition = buffer[position++];
        boolean wasDeleted = buffer[position++] != 0;
        boolean isGuard = buffer[position++] != 0;
        return Record.builder().key(key).data(data).overflowRecordPage(overflowRecordPage).overflowRecordPosition(overflowRecordPosition).wasDeleted(wasDeleted).isGuard(isGuard).build();
    }

    public Record readRecord(int pageNumber, int key) throws IOException { //pageNumber i key otrzymujemy z indeksu
        long currentPageNumber = tape.getFilePointer() / PAGE_SIZE;
        if (pageNumber != currentPageNumber) { //strony o numerze pageNumber nie ma w buforze
            tape.seek((long) pageNumber * PAGE_SIZE);

        }
        return null;
    }


    public void close() throws IOException {
        tape.close();
    }
}
