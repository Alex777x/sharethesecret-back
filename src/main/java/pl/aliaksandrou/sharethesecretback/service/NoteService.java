package pl.aliaksandrou.sharethesecretback.service;


import pl.aliaksandrou.sharethesecretback.model.NoteData;
import pl.aliaksandrou.sharethesecretback.model.NoteRequest;

public interface NoteService {
    String saveNote(NoteRequest request);

    NoteData getNote(String id);

    void deleteNote(String id);
}
