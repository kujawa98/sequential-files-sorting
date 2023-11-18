package pl.qjavascr.model;

import lombok.Builder;

@Builder
public class Record implements Comparable<Record> {
    //całkowity rozmiar rekordu to 4+30+1+1+1+1=39 bajtów
    private int key;
    private String data;
    private byte overflowRecordPage;
    private byte overflowRecordPosition;
    private boolean wasDeleted;
    private boolean isGuard;

    @Override
    public int compareTo(Record rec) {
        return Integer.compare(key, rec.key);
    }

    public String data() {
        return data;
    }

}
