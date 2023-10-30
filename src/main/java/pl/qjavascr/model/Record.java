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

        int returnCode = 0;
        for (var character : dat) {
            var first = character.toCharArray();
            for (var secondCharacter : datRec) {
                var second = secondCharacter.toCharArray();
                if (first[0] == second[0]) {
                    returnCode = 0;
                } else if (first[0] < second[0]) {
                    returnCode = -1;
                    break;
                } else {
                    returnCode = 1;
                }
            }
        }
        return returnCode;
    }

    public String data() {
        return data;
    }

}
