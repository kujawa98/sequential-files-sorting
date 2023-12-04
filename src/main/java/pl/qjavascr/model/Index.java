package pl.qjavascr.model;

import lombok.Getter;

@Getter
public class Index {

    private final int key; //całkowity rozmiar wpisu ma 4+4=8 bajtów

    public Index(int key) {
        this.key = key;
    }

}
