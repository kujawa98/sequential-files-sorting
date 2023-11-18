package pl.qjavascr.util;

public class ConstantsUtils {

    public static final int PAGE_SIZE = 39;
    public static final int RECORD_LEN = 38;
    public static final int BUFFER_SIZE = 2 * PAGE_SIZE;
    public static final double ALPHA = 0.5d;
    public static final double BETA = 0.2d;
    public static final int RECORDS_PER_PAGE = (PAGE_SIZE - 1) / RECORD_LEN;


    private ConstantsUtils() {
    }

}
