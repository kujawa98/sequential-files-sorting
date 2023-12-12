package pl.qjavascr.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Page<T> {

    private int pageNumber;
    private final List<T> data;

    public Page(int pageNumber, List<T> records) {
        this.pageNumber = pageNumber;
        this.data = records;
    }

    public Page() {
        this.pageNumber = -1;
        this.data = new ArrayList<>();
    }

}
