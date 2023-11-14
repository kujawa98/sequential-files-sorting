package pl.qjavascr.model;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;

public class Record implements Comparable<Record> {
    private final String data;

    public Record(String data) {
        this.data = data;
    }

    @Override
    public int compareTo(Record rec) {
        List<String> dataCopy = Arrays.stream(data.split("")).toList();
        List<String> recDataCopy = Arrays.stream(rec.data.split("")).toList();

        var dat = CollectionUtils.subtract(dataCopy, recDataCopy);
        var datRec = CollectionUtils.subtract(recDataCopy, dataCopy);

        if ((dat.isEmpty() || dat.contains("")) && (datRec.isEmpty() || datRec.contains(""))) {
            return 0;
        }
        if (dat.isEmpty() || dat.contains("")) {
            return -1;
        }
        if (datRec.isEmpty() || datRec.contains("")) {
            return 1;
        }

        int returnCode = 0;

        for (var character : dat) {
            var first = character.toCharArray();
            for (var secondCharacter : datRec) {
                var second = secondCharacter.toCharArray();
                if (first[0] > second[0]) {
                    returnCode = 1;
                } else if (first[0] < second[0]) {
                    returnCode = -1;
                    break;
                }
            }
            if (returnCode == 1) {
                break;
            }
        }
        return returnCode;
    }

    public String data() {
        return data;
    }

}
