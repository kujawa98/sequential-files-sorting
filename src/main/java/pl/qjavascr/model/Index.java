package pl.qjavascr.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Index {

    //całkowity rozmiar wpisu ma 5 bajtów
    private final int key;
    @Setter
    private boolean isLastOnPage;

    public Index(int key) {
        this.key = key;
    }

}
