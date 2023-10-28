package pl.qjavascr;

import pl.qjavascr.model.Record;
import pl.qjavascr.model.Tape;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        Tape tape = new Tape("src/main/resources/resource");
        var records = Arrays.stream(tape.readSingleBlock()).map(Record::new).toList();
        var lis = records.stream().sorted(Record::compareTo).toList();
        System.out.println();
    }
}