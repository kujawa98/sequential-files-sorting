package pl.qjavascr.model;

import org.junit.jupiter.api.Test;

public class RecordTest {
    Record record = new Record("aaaaaaaaaz");

    @Test
    void testSort(){
        Record other = new Record("aaaaaaaabc");

        record.compareTo(other);
    }
}
