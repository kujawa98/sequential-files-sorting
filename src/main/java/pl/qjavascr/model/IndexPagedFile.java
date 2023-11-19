package pl.qjavascr.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IndexPagedFile extends PagedFile<Index> {

    private final List<Integer> keys = new ArrayList<>();
    private int records = 0;
    private int mainAreaRecords = 0;
    private int overflowRecords = 0;
    private int deletedRecords = 0;

    protected IndexPagedFile(String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public Page<Index> readPage(int pageNumber) throws IOException {
        return null;
    }

    @Override
    public Index readData(int pageNumber, int key) throws IOException {
        return null;
    }
}
