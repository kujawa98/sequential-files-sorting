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
                return trasnformBytesToPage(i);
            }
        }
        long requestedPageNumberFilePointer = (long) (pageNumber - 1) * PAGE_SIZE;
        var len = tape.length();
        var cokur = len - PAGE_SIZE;
        if (requestedPageNumberFilePointer > cokur) {
            return new Page();
        }
        tape.seek(requestedPageNumberFilePointer);
        tape.read(buffer);
        return trasnformBytesToPage(0);
    }

    private Page trasnformBytesToPage(int position) {
        int pageNumber = buffer[position];
        List<Record> records = new ArrayList<>();
        for (int i = 0; i < RECORDS_PER_PAGE; i++) {
            var record = transformBytesToRecord(position + 1);
            records.add(record);
            position += RECORD_LEN;
            if (record.isLastOnPage()) {
                break;
            }
        }
        return new Page(pageNumber, records);
    }

    private Record transformBytesToRecord(int position) {
        int key = (buffer[position] << 24) | (buffer[position + 1] << 16) | (buffer[position + 2] << 8) | (buffer[position + 3]);
        position += 4;
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < 30; i++) {
            data.append((char) (buffer[position + i]));
        }
        position += 30;
        byte overflowRecordPage = buffer[position++];
        byte overflowRecordPosition = buffer[position++];
        boolean wasDeleted = buffer[position++] != 0;
        boolean isLastOnPage = buffer[position++] != 0;
        return Record.builder().key(key).data(data.toString()).overflowRecordPage(overflowRecordPage).overflowRecordPosition(overflowRecordPosition).wasDeleted(wasDeleted).isLastOnPage(isLastOnPage).build();
    }

    public Record readRecord(int pageNumber, int key) throws IOException { //pageNumber i key otrzymujemy z indeksu
        Page page = readPage(pageNumber);
        for (var record : page.getRecords()) {
            if (record.getKey() == key) {
                return record;
            }
        }
        return null;
    }


    public void close() throws IOException {
        tape.close();
    }
}
