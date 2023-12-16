package pl.qjavascr.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import static pl.qjavascr.util.ConstantsUtils.*;
import static pl.qjavascr.util.ConstantsUtils.RECORD_LEN;

@Getter
@Setter
public class IndexPagedFile extends PagedFile<Index> {

    private List<Integer> keys = new ArrayList<>();

    public IndexPagedFile(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public Page<Index> readPage(int pageNumber) {
        return new Page<>();
    }


    public void insertData(Index data) {
        if (keys.contains(data.getKey())) {
            System.out.println("Index already in database");
            return;
        }
        keys.add(data.getKey());
        keys.sort(Integer::compareTo);
    }

    public void writeKeysToFile() throws IOException {
        int pageNumber = 1;
        int bufferIndex = 0;
        buffer = new byte[BUFFER_SIZE];
        fileHandle.seek(0L);
        buffer[bufferIndex++] = (byte) pageNumber;
        boolean reachedPage = false;
        for (Integer key : keys) {
            buffer[bufferIndex++] = (byte) ((key & 0xFF000000) >> 24);
            buffer[bufferIndex++] = (byte) ((key & 0xFF0000) >> 16);
            buffer[bufferIndex++] = (byte) ((key & 0xFF00) >> 8);
            buffer[bufferIndex++] = (byte) ((key & 0xFF));
            if (bufferIndex == PAGE_SIZE) {
                reachedPage = true;
                writeBuffer();
                bufferIndex = 0;
                buffer[bufferIndex++] = (byte) ++pageNumber;
            }
        }
        if (!reachedPage) {
            writeBuffer();
        }
    }

    @Override
    public void writePage(Page<Index> page) {
    }

    @Override
    public void close() throws IOException {
        writeKeysToFile();
        fileHandle.close();
    }

    public void readKeys() {
        System.out.println("Keys");
        int pageNumber = 1;
        for (var key : keys) {
            System.out.println("Key " + key + " : Page number " + pageNumber++);
        }
    }
}
