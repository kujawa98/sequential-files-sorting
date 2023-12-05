package pl.qjavascr.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import static pl.qjavascr.util.ConstantsUtils.INDEXES_PER_PAGE;
import static pl.qjavascr.util.ConstantsUtils.INDEX_LEN;
import static pl.qjavascr.util.ConstantsUtils.PAGE_SIZE;

@Getter
public class IndexPagedFile extends PagedFile<Index> {

    private final List<Integer> keys = new ArrayList<>();

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
        return new Index(key);
    }

    private byte[] tranformPageToBytes(Page<Index> page) {
        byte[] bytes = new byte[PAGE_SIZE];
        bytes[0] = (byte) page.getPageNumber();
        int in = 1;
        for (var index : page.getData()) {
            bytes[in] = (byte) ((index.getKey() & 0xFF000000) >> 24);
            bytes[in + 1] = (byte) ((index.getKey() & 0xFF0000) >> 16);
            bytes[in + 2] = (byte) ((index.getKey() & 0xFF00) >> 8);
            bytes[in + 3] = (byte) ((index.getKey() & 0xFF));
            in += 4;
        }
        return bytes;
    }

    @Override
    public Index readData(int pageNumber, int key) throws IOException {
        return new Index(key);
    }


    public void insertData(Index data) throws IOException {
        if (keys.contains(data.getKey())) {
            System.out.println("Index already in database");
            return;
        }
        keys.add(data.getKey());
        keys.sort(Integer::compareTo);
        byte[] buffer = new byte[PAGE_SIZE];
        int pageNumber = 0;
        int bufferIndex = 0;
        fileHandle.seek(0L);
        buffer[bufferIndex++] = (byte) pageNumber;
        for (Integer key : keys) {
            buffer[bufferIndex++] = (byte) ((key & 0xFF000000) >> 24);
            buffer[bufferIndex++] = (byte) ((key & 0xFF0000) >> 16);
            buffer[bufferIndex++] = (byte) ((key & 0xFF00) >> 8);
            buffer[bufferIndex++] = (byte) ((key & 0xFF));
            if (bufferIndex == PAGE_SIZE) {
                pageNumber++;
            }
        }
    }

    @Override
    public void writePage(Page<Index> page) throws IOException {
        long requestedPageNumberFilePointer = (long) (page.getPageNumber() - 1) * PAGE_SIZE;
        fileHandle.seek(requestedPageNumberFilePointer);
        fileHandle.write(tranformPageToBytes(page));
    }

}
