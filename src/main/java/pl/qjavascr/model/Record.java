package pl.qjavascr.model;

import org.apache.commons.collections4.CollectionUtils;

import java.util.*;

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

        if (dat.contains("") && datRec.contains("")) {
            return 0;
        }
        if (dat.contains("")) {
            return -1;
        }
        if (datRec.contains("")) {
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
