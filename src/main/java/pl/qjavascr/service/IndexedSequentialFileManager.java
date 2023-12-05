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
            if (keys.get(i) < key && keys.get(i + 1) > key) {
                pageNumber = i;
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
                           .overflowRecordPage((byte) -1)
                           .wasDeleted(false)
                           .build());
            page.getData().sort(Record::compareTo);
            page.setPageNumber(pageNumber);
            mainDataPagedFile.writePage(page);
            records++;
            mainAreaRecords++;
        } else {
            System.out.println("Jeszcze nie teraz");
            // 3. określ miejsce gdzie powinien się znaleźć nowy rekord
            Page<Record> page = mainDataPagedFile.readPage(pageNumber);
            var recordsList = page.getData();
            int recordNumber = 0;
            for (int i = 0; i < recordsList.size() - 1; i++) {
                if (recordsList.get(i).getKey() < key && recordsList.get(i + 1).getKey() > key) {
                    recordNumber = i;
                    break;
                }
            }
            var record = recordsList.get(recordNumber);
            while (record.getOverflowRecordPosition() != -1 && record.getOverflowRecordPage() != -1) {
                Page<Record> pg = overfloDataPagedFile.readPage(record.getOverflowRecordPage());
                record = pg.getData().get(record.getOverflowRecordPosition());
            }
            //todo tutaj wpisac nowy rekord na koniec overflowa i przestawić wskaźniki

            records++;
            overflowRecords++;
        }
    }

    public void readRecord(int key) {

    }

    public void readIndexFile() {

    }

    public void readDataFile(boolean mainOnly) {

    }

    public void updateRecord(int key, String data) {

    }

    public void deleteRecord(int key) {

    }

    public void reorganize() {

    }


}