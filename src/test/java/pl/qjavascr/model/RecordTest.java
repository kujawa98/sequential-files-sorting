//package pl.qjavascr.model;
//
//import org.junit.jupiter.api.Test;
//
//public class RecordTest {
//    Record record = new Record("fyzyhjwoolevzzzsfgrkoll");
//
//
//    @Test
//    void testSort() {
//        Record other = new Record("iercpjzshhtqpevfzkb");
//
//        assert record.compareTo(other) > 0;
//    }
//
//    @Test
//    void testEmpty() {
//        Record other = new Record("fyzyhjwoolevzzzsfgrkoll");
//        assert (record.compareTo(other) == 0 && other.compareTo(record) == 0);
//        other = new Record("ffyzyhjwoolevzzzsfgrkoll");
//        assert (record.compareTo(other) < 0 && other.compareTo(record) > 0);
//    }
//
//    @Test
//    void testRealEmpty() {
//        Record other = new Record("");
//        assert record.compareTo(other) > 0 && other.compareTo(record) < 0;
//    }
//
//}
