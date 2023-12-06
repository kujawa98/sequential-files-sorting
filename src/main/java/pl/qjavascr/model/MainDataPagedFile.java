package pl.qjavascr.model;

import org.apache.commons.lang3.tuple.Pair;

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
        byte[] buffer = new byte[PAGE_SIZE];
        long requestedPageNumberFilePointer = (long) (pageNumber - 1) * PAGE_SIZE;
        if (requestedPageNumberFilePointer > fileHandle.length() - PAGE_SIZE) {
            return new Page<>();
        }
        fileHandle.seek(requestedPageNumberFilePointer);
        fileHandle.read(buffer);
        List<Record> records = new ArrayList<>();
        int position = 1;
        for (int i = 0; i < RECORDS_PER_PAGE; i++) {
            var record = transformBytesToRecord(buffer, position);
            records.add(record);
            position += RECORD_LEN;
            if (record.isLastOnPage()) {
                break;
            }
        }
        return new Page<>(pageNumber, records);
    }

    private Record transformBytesToRecord(byte[] buffer, int position) {
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
        return page.getData()
                .stream()
                .filter(record -> record.getKey() == key)
                .findFirst()
                .orElse(Record.builder().build());
    }

    @Override
    public synchronized void writePage(Page<Record> page) throws IOException {
        long requestedPageNumberFilePointer = (long) (page.getPageNumber() - 1) * PAGE_SIZE;
        fileHandle.seek(requestedPageNumberFilePointer);
        var records = page.getData();
        byte[] buffer = new byte[PAGE_SIZE];
        buffer[0] = (byte) page.getPageNumber();
        int position = 1;
        for (int i = 0; i < records.size(); i++) {
            var record = records.get(i);
            buffer[position] = (byte) ((record.getKey() & 0xFF000000) >> 24);
            buffer[position + 1] = (byte) ((record.getKey() & 0xFF0000) >> 16);
            buffer[position + 2] = (byte) ((record.getKey() & 0xFF00) >> 8);
            buffer[position + 3] = (byte) ((record.getKey() & 0xFF));
            position += 4;
            for (int j = 0; j < 30; j++) {
                buffer[position++] = record.getData().getBytes()[j];
            }
            buffer[position++] = record.getOverflowRecordPage();
            buffer[position++] = record.getOverflowRecordPosition();
            buffer[position++] = (byte) (record.isWasDeleted() ? 1 : 0);
            buffer[position++] = (byte) (i == records.size() - 1 ? 1 : 0);
        }
        fileHandle.write(buffer);
    }

    public boolean isPageFree(int pageNumber) throws IOException {
        var page = readPage(pageNumber);
        return page.getData().size() < RECORDS_PER_PAGE;
    }

    public Pair<Integer, Integer> writeAtTheEnd(Record record) throws IOException {
        long fileLenght = fileHandle.length();
        int pageNumber = Math.toIntExact(fileLenght / PAGE_SIZE);
        pageNumber = pageNumber == 0 ? 1 : pageNumber;
        if (isPageFree(pageNumber)) {
            Page<Record> page = readPage(pageNumber);
            page.getData().add(record);
            page.setPageNumber(pageNumber);
            writePage(page);
            return Pair.of(pageNumber, page.getData().indexOf(record));
        } else {
            fileHandle.seek(fileLenght);
            var records = new ArrayList<Record>();
            records.add(record);
            Page<Record> page = new Page<>(pageNumber + 1, records);
            writePage(page);
            return Pair.of(pageNumber + 1, page.getData().indexOf(record));
        }

    }

}
