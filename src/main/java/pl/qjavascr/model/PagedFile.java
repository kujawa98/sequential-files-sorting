package pl.qjavascr.model;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import static pl.qjavascr.util.ConstantsUtils.BUFFER_SIZE;

public abstract class PagedFile<T> {

    protected final RandomAccessFile fileHandle;
    protected final byte[]           buffer;

    protected PagedFile(String fileName) throws IOException {
        this.fileHandle = new RandomAccessFile(fileName, "rw");
        this.buffer = new byte[BUFFER_SIZE];
        this.fileHandle.read(this.buffer);
    }

    public abstract Page<T> readPage(int pageNumber) throws IOException;

    public abstract T readData(int pageNumber, int key) throws IOException;

    public abstract void writePage(Page<T> page) throws IOException;

    public List<Page<T>> readWholeFile() throws IOException {
        List<Page<T>> pages = new ArrayList<>();
        Page<T> page = readPage(1);
        int pageNumber = 1;
        while (page.getPageNumber() != -1) {
            pages.add(page);
            pageNumber++;
            page = readPage(pageNumber);
        }
        return pages;
    }


}
