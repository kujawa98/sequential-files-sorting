package pl.qjavascr.service;

import java.io.IOException;

import pl.qjavascr.model.Index;
import pl.qjavascr.model.IndexPagedFile;
import pl.qjavascr.model.MainDataPagedFile;
import pl.qjavascr.model.Page;
import pl.qjavascr.model.Record;

public class IndexedSequentialFileManager {

    private final IndexPagedFile    indexPagedFile;
    private final MainDataPagedFile mainDataPagedFile;
    private final MainDataPagedFile overfloDataPagedFile;

    private int records         = 0;
    private int mainAreaRecords = 0;
    private int overflowRecords = 0;
    private int deletedRecords  = 0;

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
            return;
        }
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
                var r = pg.getData().stream().filter(record1 -> record1.getKey() == finalRecord.getKey()).findFirst().get();
                r.setOverflowRecordPage(pair.getLeft().byteValue());
                r.setOverflowRecordPosition(pair.getRight().byteValue());
                overfloDataPagedFile.writePage(pg);
            } else {
                record.setOverflowRecordPage(pair.getLeft().byteValue());
                record.setOverflowRecordPosition(pair.getRight().byteValue());
                mainDataPagedFile.writePage(page);
            }
//todo a jakby po posortowaniu obszaru nadmiarowego "prześledzać" drogę kazdego rekordu i odpowiednio ustawiać wskaźniki?
        }
    }

    public void readRecord(int key) {

    }

    public void readIndexFile() {

    }

    public void readDataFile(boolean mainOnly) {

    }

    public void updateRecord(int key, String data) throws IOException {

    }

    public void deleteRecord(int key) {

    }

    public void reorganize() {

    }


}
