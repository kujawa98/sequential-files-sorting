package pl.qjavascr.model;

import org.junit.jupiter.api.Test;

public class RecordTest {
    Record record = new Record("fyzyhjwoolevsfgrkoll");


    @Test
    void testSort(){
        Record other = new Record("iercpjzzshhtqpevfzkb");

        record.compareTo(other);
    }
}
