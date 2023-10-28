package pl.qjavascr;

import pl.qjavascr.model.Tape;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Tape tape = new Tape("src/main/resources/resource");
        tape.readSingleBlock();
        for (int i = 0; i < 12; i++) {
            var bl = tape.readSingleRecord();
            bl.print();
        }
        tape.close();
        System.out.println();
    }
}