package pl.qjavascr.util;

public class ConstantsUtils {

    public static final int PAGE_SIZE = 153;
    public static final int RECORD_LEN = 38;
    public static final int INDEX_LEN = 4;
    public static final int BUFFER_SIZE = PAGE_SIZE;
    public static final double ALPHA = 0.5d;
    public static final double BETA = 0.2d;
    public static final int RECORDS_PER_PAGE = (PAGE_SIZE - 1) / RECORD_LEN;
    public static final int INDEXES_PER_PAGE = (PAGE_SIZE - 1) / INDEX_LEN;
    public static int writes = 0;
    public static int reads = 0;


    private ConstantsUtils() {
    }

}
