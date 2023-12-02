package pl.qjavascr.utils;

import org.junit.jupiter.api.Test;
import pl.qjavascr.model.ReadingTape;
import pl.qjavascr.service.RandomGenerator;

import java.io.IOException;

import static pl.qjavascr.util.SeriesUtils.howManySeries;

public class SeriesUtilsTest {
    String output = "src/main/resources/output.dat";

    @Test
    void testSeries() throws IOException {
        RandomGenerator.randomTape(1000);
        System.out.println(howManySeries(new ReadingTape(output,false)));
    }
}
