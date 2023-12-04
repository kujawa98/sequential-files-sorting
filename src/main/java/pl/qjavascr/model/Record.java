package pl.qjavascr.model;

import lombok.Builder;
import lombok.Getter;

@Builder
public class Record implements Comparable<Record> {

    //całkowity rozmiar rekordu to 4+30+1+1+1+1=38 bajtów
    @Getter
    private int     key;
    private String  data;
    private byte    overflowRecordPage;
    private byte    overflowRecordPosition;
    private boolean wasDeleted;
    @Getter
    private boolean isLastOnPage;

    @Override
    public int compareTo(Record rec) {
        return Integer.compare(key, rec.key);
    }

    public String data() {
        return data;
    }

}
