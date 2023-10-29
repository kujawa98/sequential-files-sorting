package pl.qjavascr;

import pl.qjavascr.model.Tape;
import pl.qjavascr.sorter.Sorter;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
//        Tape tape = new Tape("src/main/resources/resource");
//        tape.readSingleBlock();
//        for (int i = 0; i < 14; i++) {
//            var bl = tape.readSingleRecord();
//            bl.print();
//        }
//        tape.close();
//        System.out.println();


        Tape tape = new Tape("src/main/resources/resource");
        tape.readSingleBlock();
        Sorter sorter = new Sorter();
        Tape tape1 = new Tape("src/main/resources/tape1");
        Tape tape2 = new Tape("src/main/resources/tape2");
        sorter.distribute(tape, tape1, tape2);
    }
}