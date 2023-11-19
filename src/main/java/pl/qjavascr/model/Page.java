package pl.qjavascr.model;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

public class Page<T> {
    private final int pageNumber;
    @Getter
    private final List<T> data;

    public Page(int pageNumber, List<T> records) {
        this.pageNumber = pageNumber;
        this.data = records;
    }

    public Page() {
        this.pageNumber = -1;
        this.data = Collections.emptyList();
    }
}
