package pl.qjavascr.sorter;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import pl.qjavascr.model.ReadingTape;
import pl.qjavascr.model.WritingTape;
import pl.qjavascr.service.RandomGenerator;

import static pl.qjavascr.util.ConstantsUtils.writes;

class SorterTest {

    Sorter sorter = new Sorter();


    @Test
    void testDistribution() throws IOException {
        WritingTape writingTape1 = new WritingTape("src/test/resources/writingTape1.txt");
        WritingTape writingTape2 = new WritingTape("src/test/resources/writingTape2.txt");
        ReadingTape outputReadingTape = new ReadingTape("src/test/resources/resource.txt");

        sorter.distribute(outputReadingTape, writingTape1, writingTape2);

    }

    @Test
    void testMerge() throws IOException {
        ReadingTape readingTape1 = new ReadingTape("src/test/resources/writingTape1.txt");
        ReadingTape readingTape2 = new ReadingTape("src/test/resources/writingTape2.txt");
        WritingTape writingTape = new WritingTape("src/test/resources/writing.txt");

        sorter.merge(readingTape1, readingTape2, writingTape);
    }

    @Test
    void testSort() throws IOException {
        RandomGenerator.randomTape(1000);
        sorter.sort();
        System.out.println(writes);
    }

}
