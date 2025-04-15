package pl.aliaksandrou.sharethesecretback.storage;


import pl.aliaksandrou.sharethesecretback.model.NoteData;
import pl.aliaksandrou.sharethesecretback.model.NoteRequest;

public interface NoteStorage {
    void save(String id, NoteRequest request);

    NoteData get(String id);

    void delete(String id);
}
