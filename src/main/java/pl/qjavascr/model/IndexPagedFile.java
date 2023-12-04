package pl.qjavascr.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static pl.qjavascr.util.ConstantsUtils.INDEXES_PER_PAGE;
import static pl.qjavascr.util.ConstantsUtils.INDEX_LEN;
import static pl.qjavascr.util.ConstantsUtils.PAGE_SIZE;
import static pl.qjavascr.util.ConstantsUtils.RECORDS_PER_PAGE;
import static pl.qjavascr.util.ConstantsUtils.RECORD_LEN;

public class IndexPagedFile extends PagedFile<Index> {

    private final List<Integer> keys            = new ArrayList<>();
    private       int           records         = 0;
    private       int           mainAreaRecords = 0;
    private       int           overflowRecords = 0;
    private       int           deletedRecords  = 0;

    protected IndexPagedFile(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public Page<Index> readPage(int pageNumber) throws IOException {
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

    private Page<Index> trasnformBytesToPage(int position) {
        int pageNumber = buffer[position];
        List<Index> indexes = new ArrayList<>();
        for (int i = 0; i < INDEXES_PER_PAGE; i++) {
            Index index = transformBytesToIndex(position + 1);
            indexes.add(index);
            position += INDEX_LEN;
        }
        return new Page<>(pageNumber, indexes);
    }

    private Index transformBytesToIndex(int position) {
        int key = (buffer[position] << 24) | (buffer[position + 1] << 16) | (buffer[position + 2] << 8) | (buffer[position + 3]);
        position += 4;
        int pageNumber = (buffer[position] << 24) | (buffer[position + 1] << 16) | (buffer[position + 2] << 8) | (buffer[position + 3]);
        return new Index(key, pageNumber);
    }

    @Override
    public Index readData(int pageNumber, int key) throws IOException {
        return null;
    }

}
