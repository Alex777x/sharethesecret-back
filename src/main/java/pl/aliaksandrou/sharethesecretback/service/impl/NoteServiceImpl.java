package pl.aliaksandrou.sharethesecretback.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.aliaksandrou.sharethesecretback.model.NoteData;
import pl.aliaksandrou.sharethesecretback.model.NoteRequest;
import pl.aliaksandrou.sharethesecretback.service.NoteService;
import pl.aliaksandrou.sharethesecretback.storage.NoteStorage;

import java.util.UUID;

@Service
@AllArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteStorage storage;

    @Override
    public String saveNote(NoteRequest request) {
        String id = UUID.randomUUID().toString();
        storage.save(id, request);
        return id;
    }

    @Override
    public NoteData getNote(String id) {
        NoteData note = storage.get(id);
        if (note != null && "1-view".equals(note.getTtl())) {
            storage.delete(id);
        }
        return note;
    }

    @Override
    public void deleteNote(String id) {
        storage.delete(id);
    }
}
