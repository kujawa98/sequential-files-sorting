package pl.qjavascr.model;

import java.io.IOException;
import java.io.RandomAccessFile;

import static pl.qjavascr.util.ConstantsUtils.BUFFER_SIZE;

public abstract class PagedFile<T> {
    protected final RandomAccessFile fileHandle;
    protected final byte[] buffer;

    protected PagedFile(String fileName) throws IOException {
        this.fileHandle = new RandomAccessFile(fileName, "r");
        this.buffer = new byte[BUFFER_SIZE];
        this.fileHandle.read(this.buffer);
    }

    public abstract Page<T> readPage(int pageNumber) throws IOException;

    public abstract T readData(int pageNumber, int key) throws IOException;

}
