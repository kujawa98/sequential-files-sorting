package pl.qjavascr.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.qjavascr.model.Index;
import pl.qjavascr.model.IndexPagedFile;
import pl.qjavascr.model.MainDataPagedFile;
import pl.qjavascr.model.Page;
import pl.qjavascr.model.Record;

import static pl.qjavascr.util.ConstantsUtils.ALPHA;
import static pl.qjavascr.util.ConstantsUtils.RECORDS_PER_PAGE;

public class IndexedSequentialFileManager {

    private final IndexPagedFile indexPagedFile;
    private final MainDataPagedFile mainDataPagedFile;
    private final MainDataPagedFile overfloDataPagedFile;

    private int records = 0;
    private int mainAreaRecords = 0;
    private int overflowRecords = 0;
    private int deletedRecords = 0;

    public IndexedSequentialFileManager(IndexPagedFile indexPagedFile,
                                        MainDataPagedFile mainDataPagedFile,
                                        MainDataPagedFile overfloDataPagedFile) {
        this.indexPagedFile = indexPagedFile;
        this.mainDataPagedFile = mainDataPagedFile;
        this.overfloDataPagedFile = overfloDataPagedFile;
    }


    public void addRecord(int key, String data) throws IOException {
        // 1. znajdź stronę z indeksu na której powinieneś zapisać
        var keys = indexPagedFile.getKeys();
        if (keys.contains(key)) {
            System.out.println("Klucze nie mogą się powtarzać");
            return;
        }
        if (keys.isEmpty()) {
            indexPagedFile.insertData(new Index(key));
            Page<Record> page = mainDataPagedFile.readPage(1);
            page.getData()
                    .add(Record.builder()
                            .data(data)
                            .key(key)
                            .overflowRecordPage((byte) -1)
                            .overflowRecordPosition((byte) -1)
                            .wasDeleted(false)
                            .build());
            page.getData().sort(Record::compareTo);
            page.setPageNumber(1);
            mainDataPagedFile.writePage(page);
            records++;
            mainAreaRecords++;
            return;
        }
        //do której strony powinienem zapisać
        int pageNumber = keys.size();
        for (int i = 0; i < keys.size() - 1; i++) {
            if ((keys.get(i) < key && keys.get(i + 1) > key)) {
                pageNumber = i;
                break;
            } else if (i + 1 == keys.size() - 1) {
                pageNumber = i + 1;
                break;
            }
        }
        // 2. jeżeli strona w głównym obszarze danych jest wolna to zapisz, jeżeli nie to zapisz do obszaru nadmiarowego
        if (mainDataPagedFile.isPageFree(pageNumber)) {
            Page<Record> page = mainDataPagedFile.readPage(pageNumber);
            page.getData()
                    .add(Record.builder()
                            .data(data)
                            .key(key)
                            .overflowRecordPage((byte) -1)
                            .overflowRecordPosition((byte) -1)
                            .wasDeleted(false)
                            .build());
            page.getData().sort(Record::compareTo);
            page.setPageNumber(pageNumber);
            mainDataPagedFile.writePage(page);
            records++;
            mainAreaRecords++;
        } else {
            // 3. określ miejsce gdzie powinien się znaleźć nowy rekord
            Page<Record> page = mainDataPagedFile.readPage(pageNumber);
            var recordsList = page.getData();
            int recordNumber = 0;
            for (int i = 0; i < recordsList.size() - 1; i++) {
                if ((recordsList.get(i).getKey() < key && recordsList.get(i + 1).getKey() > key)) {
                    recordNumber = i;
                    break;
                } else if (i + 1 == recordsList.size() - 1) {
                    recordNumber = i + 1;
                    break;
                }
            }
            //idziemy z chainem do pierwszego bez wskaźnikowego elementu
            var record = recordsList.get(recordNumber);
            boolean toOverflow = false;
            Page<Record> pg = new Page<>();
            while (record.getOverflowRecordPosition() != -1 && record.getOverflowRecordPage() != -1) {
                pg = overfloDataPagedFile.readPage(record.getOverflowRecordPage());
                record = pg.getData().get(record.getOverflowRecordPosition());
                toOverflow = true; // todo sprawdzać czy rekord obecny ma większy klucz niż nowy, jeśli tak to je zamień
                if (record.getKey() > key) {
                    String temp = data;
                    data = record.getData();
                    record.setData(temp);

                    int tempK = key;
                    key = record.getKey();
                    record.setKey(tempK);

                    overfloDataPagedFile.writePage(pg);
                }
            }
            //todo tutaj wpisac nowy rekord na koniec overflowa i przestawić wskaźniki
            var pair = overfloDataPagedFile.writeAtTheEnd(Record.builder()
                    .data(data)
                    .key(key)
                    .overflowRecordPage((byte) -1)
                    .overflowRecordPosition((byte) -1)
                    .wasDeleted(false)
                    .isLastOnPage(false)
                    .build());
            records++;
            overflowRecords++;
            if (toOverflow) {
                pg = overfloDataPagedFile.readPage(pg.getPageNumber());
                Record finalRecord = record;
                var r = pg.getData()
                        .stream()
                        .filter(record1 -> record1.getKey() == finalRecord.getKey())
                        .findFirst()
                        .get();
                r.setOverflowRecordPage(pair.getLeft().byteValue());
                r.setOverflowRecordPosition(pair.getRight().byteValue());
                overfloDataPagedFile.writePage(pg);
            } else {
                record.setOverflowRecordPage(pair.getLeft().byteValue());
                record.setOverflowRecordPosition(pair.getRight().byteValue());
                mainDataPagedFile.writePage(page);
            }
        }
    }

    public void readRecord(int key) throws IOException {
        //wyszukaj stronę gdzie powinien być rekord, jeśli nie to idziesz za wskaźnikiem
        var keys = indexPagedFile.getKeys();
        int pageNumber = keys.size();
        for (int i = 0; i < keys.size() - 1; i++) {
            if ((keys.get(i) < key && keys.get(i + 1) > key)) {
                pageNumber = i;
                break;
            } else if (i + 1 == keys.size() - 1) {
                pageNumber = i + 1;
                break;
            }
        }
        var page = mainDataPagedFile.readPage(pageNumber);
        for (var record : page.getData()) {
            if (record.getKey() == key) {
                System.out.println(record.getKey());
                System.out.println(record.getData());
                return;
            }
        }
        //podążaj za wskaźnikiem
        var recordsList = page.getData();
        int recordNumber = 0;
        for (int i = 0; i < recordsList.size() - 1; i++) {
            if ((recordsList.get(i).getKey() < key && recordsList.get(i + 1).getKey() > key)) {
                recordNumber = i;
                break;
            } else if (i + 1 == recordsList.size() - 1) {
                recordNumber = i + 1;
                break;
            }
        }
        var record = page.getData().get(recordNumber);
        if (record.getKey() == key) {
            System.out.println(record.getKey());
            System.out.println(record.getData());
            return;
        }
        while (true) {
            if (record.getOverflowRecordPosition() != -1 && record.getOverflowRecordPage() != -1) {
                record = overfloDataPagedFile.readPage(record.getOverflowRecordPage()).getData().get(record.getOverflowRecordPosition());
            } else {
                System.out.println("Nie ma rekordu z kluczem " + key);
                return;
            }
            if (record.getKey() == key) {
                System.out.println(record.getKey());
                System.out.println(record.getData());
                return;
            }
        }
    }

    public void readIndexFile() throws IOException {
        var i = indexPagedFile.readWholeFile();
        System.out.println(i);
    }

    public void readDataFile(boolean mainOnly) throws IOException {
        var s = mainDataPagedFile.readWholeFile();
        System.out.println(s);
    }

    public void updateRecord(int key, String data) throws IOException {
        //wyszukaj stronę gdzie powinien być rekord, jeśli nie to idziesz za wskaźnikiem
        var keys = indexPagedFile.getKeys();
        int pageNumber = keys.size();
        for (int i = 0; i < keys.size() - 1; i++) {
            if ((keys.get(i) < key && keys.get(i + 1) > key)) {
                pageNumber = i;
                break;
            } else if (i + 1 == keys.size() - 1) {
                pageNumber = i + 1;
                break;
            }
        }
        var page = mainDataPagedFile.readPage(pageNumber);
        for (int i = 0; i < page.getData().size(); i++) {
            if (page.getData().get(i).getKey() == key) {
                page.getData().get(i).setData(data);
                mainDataPagedFile.writePage(page);
                return;
            }
        }
        //podążaj za wskaźnikiem
        var recordsList = page.getData();
        int recordNumber = 0;
        for (int i = 0; i < recordsList.size() - 1; i++) {
            if ((recordsList.get(i).getKey() < key && recordsList.get(i + 1).getKey() > key)) {
                recordNumber = i;
                break;
            } else if (i + 1 == recordsList.size() - 1) {
                recordNumber = i + 1;
                break;
            }
        }
        var record = page.getData().get(recordNumber);
        if (record.getKey() == key) {
            record.setData(data);
            mainDataPagedFile.writePage(page);
            return;
        }
        Page<Record> overflowPage = new Page<>();
        while (true) {
            if (record.getOverflowRecordPosition() != -1 && record.getOverflowRecordPage() != -1) {
                overflowPage = overfloDataPagedFile.readPage(record.getOverflowRecordPage());
                record = overflowPage.getData().get(record.getOverflowRecordPosition());
            } else {
                System.out.println("Nie ma rekordu z kluczem " + key);
                return;
            }
            if (record.getKey() == key) {
                record.setData(data);
                overfloDataPagedFile.writePage(overflowPage);
                return;
            }
        }
    }

    public void deleteRecord(int key) throws IOException {
        //wyszukaj stronę gdzie powinien być rekord, jeśli nie to idziesz za wskaźnikiem
        var keys = indexPagedFile.getKeys();
        int pageNumber = keys.size();
        for (int i = 0; i < keys.size() - 1; i++) {
            if ((keys.get(i) < key && keys.get(i + 1) > key)) {
                pageNumber = i;
                break;
            } else if (i + 1 == keys.size() - 1) {
                pageNumber = i + 1;
                break;
            }
        }
        var page = mainDataPagedFile.readPage(pageNumber);
        for (int i = 0; i < page.getData().size(); i++) {
            if (page.getData().get(i).getKey() == key) {
                page.getData().get(i).setWasDeleted(true);
                mainDataPagedFile.writePage(page);
                return;
            }
        }
        //podążaj za wskaźnikiem
        var recordsList = page.getData();
        int recordNumber = 0;
        for (int i = 0; i < recordsList.size() - 1; i++) {
            if ((recordsList.get(i).getKey() < key && recordsList.get(i + 1).getKey() > key)) {
                recordNumber = i;
                break;
            } else if (i + 1 == recordsList.size() - 1) {
                recordNumber = i + 1;
                break;
            }
        }
        var record = page.getData().get(recordNumber);
        if (record.getKey() == key) {
            record.setWasDeleted(true);
            mainDataPagedFile.writePage(page);
            return;
        }
        Page<Record> overflowPage = new Page<>();
        while (true) {
            if (record.getOverflowRecordPosition() != -1 && record.getOverflowRecordPage() != -1) {
                overflowPage = overfloDataPagedFile.readPage(record.getOverflowRecordPage());
                record = overflowPage.getData().get(record.getOverflowRecordPosition());
            } else {
                System.out.println("Nie ma rekordu z kluczem " + key);
            }
            if (record.getKey() == key) {
                record.setWasDeleted(true);
                overfloDataPagedFile.writePage(overflowPage);
                return;
            }
        }
    }

    public void reorganize() throws IOException {
        //todo algorytm reorganizacji pliku indeksowo-sekwencyjnego
        //todo wiadomo ile będzie rekordów a co za tym idzie wiadomo ile będzie stron
        //todo wypełniam stronę i zapisuję - przez sekwencje mam gwarancje ze bedzie git
        MainDataPagedFile newMainDataPagedFile = new MainDataPagedFile("src/test/resources/newMain.dat");
        IndexPagedFile newIndexPagedFile = new IndexPagedFile("src/test/resources/newIndex.idx");

        Page<Record> page = mainDataPagedFile.readPage(1);
        List<Record> records = page.getData();
        int currentWritePage = 1;
        int currentReadPage = 1;
        int alpha = (int) (ALPHA * RECORDS_PER_PAGE);
        int recordsWritten = 0;
        List<Record> newRecords = new ArrayList<>(); //to wypełniamymy

        while (page.getPageNumber() != -1) {

            for (Record rec : records) { //dla każdego rekordu ze strony w obszarze głównym

                if (!rec.isWasDeleted()) { //jeżeli nie jest do usunięcia
                    newRecords.add(Record.builder().key(rec.getKey()).data(rec.data()).wasDeleted(false).isLastOnPage(false).overflowRecordPosition((byte) -1).overflowRecordPage((byte) -1).build());
                    recordsWritten++;
                }

                if (recordsWritten == alpha) { //jeżeli wypełniliśmy alfa rekordów na stronie
                    recordsWritten = 0;
                    newIndexPagedFile.insertData(new Index(newRecords.getFirst().getKey()));
                    Page<Record> newPage = new Page<>(currentWritePage++, newRecords);
                    newMainDataPagedFile.writePage(newPage);
                    newRecords = new ArrayList<>();
                }

                //podążamy za wskaźnikami
                Record temp = rec;
                while (temp.getOverflowRecordPosition() != -1 && temp.getOverflowRecordPage() != -1) {
                    temp = overfloDataPagedFile.readPage(temp.getOverflowRecordPage()).getData().get(temp.getOverflowRecordPosition());
                    if (!temp.isWasDeleted()) {
                        newRecords.add(Record.builder().key(temp.getKey()).data(temp.data()).wasDeleted(false).isLastOnPage(false).overflowRecordPosition((byte) -1).overflowRecordPage((byte) -1).build());
                        recordsWritten++;
                    }
                    if (recordsWritten == alpha) { //jeżeli wypełniliśmy alfa rekordów na stronie
                        recordsWritten = 0;
                        newIndexPagedFile.insertData(new Index(newRecords.getFirst().getKey()));
                        Page<Record> newPage = new Page<>(currentWritePage++, newRecords);
                        newMainDataPagedFile.writePage(newPage);
                        newRecords = new ArrayList<>();
                    }
                }
            }
            page = mainDataPagedFile.readPage(++currentReadPage);
            records = page.getData();
        }
        overflowRecords = 0;
        mainAreaRecords = this.records;
        if (!newRecords.isEmpty()) {
            newMainDataPagedFile.writePage(new Page<>(currentWritePage++, newRecords));
            newMainDataPagedFile.writeBuffer();
        } else {
            newMainDataPagedFile.writeBuffer();
        }
        mainDataPagedFile.copy("src/test/resources/newMain.dat");
    }

    public void close() throws IOException {
        mainDataPagedFile.close();
        overfloDataPagedFile.close();
        indexPagedFile.close();
    }

}
