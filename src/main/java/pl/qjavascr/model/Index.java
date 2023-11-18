package pl.qjavascr.model;

import java.util.ArrayList;
import java.util.List;

public class Index {

    private final List<Integer> keys = new ArrayList<>();
    private int records = 0;
    private int mainAreaRecords = 0;
    private int overflowRecords = 0;
    private int deletedRecords = 0;

}
