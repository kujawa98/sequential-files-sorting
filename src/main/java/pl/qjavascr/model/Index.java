package pl.qjavascr.model;

import lombok.Getter;

@Getter
public class Index {

    //całkowity rozmiar wpisu ma 4 bajty
    private final int key;

    public Index(int key) {
        this.key = key;
    }

}
