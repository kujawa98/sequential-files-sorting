package pl.qjavascr.model;

import java.util.Collections;
import java.util.List;

public class Page {
    private final int pageNumber;
    private final List<Record> records;

    public Page(int pageNumber, List<Record> records) {
        this.pageNumber = pageNumber;
        this.records = records;
    }

    public Page() {
        this.pageNumber = -1;
        this.records = Collections.emptyList();
    }
}
