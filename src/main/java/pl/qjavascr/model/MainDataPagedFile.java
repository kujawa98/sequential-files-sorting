package pl.qjavascr.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static pl.qjavascr.util.ConstantsUtils.PAGE_SIZE;
import static pl.qjavascr.util.ConstantsUtils.RECORDS_PER_PAGE;
import static pl.qjavascr.util.ConstantsUtils.RECORD_LEN;

public class MainDataPagedFile extends PagedFile<Record> {

    protected MainDataPagedFile(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public Page<Record> readPage(int pageNumber) throws IOException {
        for (int i = 0; i < buffer.length; i += PAGE_SIZE) {
            int currentPageNumber = buffer[i];
            if (currentPageNumber == pageNumber) {
                return trasnformBytesToPage(i);
            }
        }
        long requestedPageNumberFilePointer = (long) (pageNumber - 1) * PAGE_SIZE;
        if (requestedPageNumberFilePointer > fileHandle.length() - PAGE_SIZE) {
            return new Page<>();
        }
        fileHandle.seek(requestedPageNumberFilePointer);
        fileHandle.read(buffer);
        return trasnformBytesToPage(0);
    }

    private Page<Record> trasnformBytesToPage(int position) {
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
        return new Page<>(pageNumber, records);
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
        return Record.builder()
                .key(key)
                .data(data.toString())
                .overflowRecordPage(overflowRecordPage)
                .overflowRecordPosition(overflowRecordPosition)
                .wasDeleted(wasDeleted)
                .isLastOnPage(isLastOnPage)
                .build();
    }

    @Override
    public Record readData(int pageNumber, int key) throws IOException { //pageNumber i key otrzymujemy z indeksu
        Page<Record> page = readPage(pageNumber);
        for (Record record : page.getData()) {
            if (record.getKey() == key) {
                return record;
            }
        }
        return Record.builder().build();
    }

    @Override
    public void insertData(Record data) throws IOException {
        List<Page<Record>> pages = new ArrayList<>();
        Page<Record> page = readPage(0);
        int pageNumber = 1;
        while (page.getPageNumber() != -1) {
            page.getData()
        }
    }

}
