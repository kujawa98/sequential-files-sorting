package pl.qjavascr.model;

public class Index {
    private final int key; //całkowity rozmiar wpisu ma 4+4=8 bajtów
    private final int pageNumber;

    public Index(int key, int pageNumber) {
        this.key = key;
        this.pageNumber = pageNumber;
    }
}
