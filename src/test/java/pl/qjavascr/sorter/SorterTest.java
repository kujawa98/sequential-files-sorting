package pl.qjavascr.sorter;

import org.junit.jupiter.api.Test;
import pl.qjavascr.model.ReadingTape;
import pl.qjavascr.model.WritingTape;

import java.io.FileNotFoundException;
import java.io.IOException;

public class SorterTest {
    Sorter sorter = new Sorter();


    @Test
    void testDistribution() throws IOException {
        WritingTape writingTape1 = new WritingTape("src/test/resources/readingTape1.txt");
        WritingTape writingTape2 = new WritingTape("src/test/resources/readingTape2.txt");
        ReadingTape outputReadingTape = new ReadingTape("src/test/resources/resource.txt");

        sorter.distribute(outputReadingTape, writingTape1, writingTape2);

    }
}
