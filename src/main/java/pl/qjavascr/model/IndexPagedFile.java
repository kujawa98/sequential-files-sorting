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
//        if (buffer[0] == pageNumber) { //strona jest w buforze
//            List<Index> indexes = new ArrayList<>();
//            int position = 1;
//            for (int i = 0; i < INDEXES_PER_PAGE; i++) {
//                var index = transformBytesToIndex(position);
//                indexes.add(index);
//                position += INDEX_LEN;
//                if (index.isLastOnPage()) {
//                    break;
//                }
//            }
//            return new Page<>(pageNumber, indexes);
//        } else {
//            writeBuffer();
//        }
//
//        //czytaj stronę do bufora
//        long requestedPageNumberFilePointer = (long) (pageNumber - 1) * PAGE_SIZE;
//        if (requestedPageNumberFilePointer > fileHandle.length() - PAGE_SIZE) {
//            return new Page<>();
//        }
//        fileHandle.seek(requestedPageNumberFilePointer);
//        fileHandle.read(buffer);
//        List<Index> indexes = new ArrayList<>();
//        int position = 1;
//        for (int i = 0; i < INDEXES_PER_PAGE; i++) {
//            var index = transformBytesToIndex(position);
//            indexes.add(index);
//            position += INDEX_LEN;
//            if (index.isLastOnPage()) {
//                break;
//            }
//        }
//        return new Page<>(pageNumber, indexes);
        return new Page<>();
    }

//    private Index transformBytesToIndex(int position) {
//        int key = (buffer[position] << 24) | (buffer[position + 1] << 16) | (buffer[position + 2] << 8) | (buffer[position + 3]);
//        return new Index(key);
//    }


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
        //strona jest w buforze
//        if (buffer[0] == page.getPageNumber()) {
//            var indexes = page.getData();
//            int position = 1;
//            for (int i = 0; i < indexes.size(); i++) {
//                var index = indexes.get(i);
//                buffer[position] = (byte) ((index.getKey() & 0xFF000000) >> 24);
//                buffer[position + 1] = (byte) ((index.getKey() & 0xFF0000) >> 16);
//                buffer[position + 2] = (byte) ((index.getKey() & 0xFF00) >> 8);
//                buffer[position + 3] = (byte) ((index.getKey() & 0xFF));
//            }
//            return;
//        } else {
//            writeBuffer();
//        }
//        //strony nie ma w buforze
//        long requestedPageNumberFilePointer = (long) (page.getPageNumber() - 1) * PAGE_SIZE;
//        fileHandle.seek(requestedPageNumberFilePointer);
//        var indexes = page.getData();
//        buffer[0] = (byte) page.getPageNumber();
//        int position = 1;
//        for (int i = 0; i < indexes.size(); i++) {
//            var index = indexes.get(i);
//            buffer[position] = (byte) ((index.getKey() & 0xFF000000) >> 24);
//            buffer[position + 1] = (byte) ((index.getKey() & 0xFF0000) >> 16);
//            buffer[position + 2] = (byte) ((index.getKey() & 0xFF00) >> 8);
//            buffer[position + 3] = (byte) ((index.getKey() & 0xFF));
//        }
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
