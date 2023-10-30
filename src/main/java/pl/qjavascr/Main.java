package pl.qjavascr;

import pl.qjavascr.model.ReadingTape;
import pl.qjavascr.sorter.Sorter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ReadingTape readingTape = new ReadingTape("src/main/resources/resource.txt");
        for (int i = 0; i < 5; i++) {
            var bl = readingTape.readRecord();
            System.out.println(bl.data());
        }
        var bl = readingTape.readRecord();
        readingTape.close();
        System.out.println();


//        ReadingTape readingTape = new ReadingTape("src/main/resources/resource.txt");
//        readingTape.readRecord();
//        Sorter sorter = new Sorter();
//        ReadingTape readingTape1 = new ReadingTape("src/main/resources/readingTape1");
//        ReadingTape readingTape2 = new ReadingTape("src/main/resources/readingTape2");
//        sorter.distribute(readingTape, readingTape1, readingTape2);
    }
}