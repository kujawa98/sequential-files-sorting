package pl.qjavascr.model;

import org.junit.jupiter.api.Test;

public class RecordTest {
    Record record = new Record("fyzyhjwoolevzzzsfgrkoll");


    @Test
    void testSort() {
        Record other = new Record("iercpjzshhtqpevfzkb");

        assert record.compareTo(other) > 0;
    }
}
