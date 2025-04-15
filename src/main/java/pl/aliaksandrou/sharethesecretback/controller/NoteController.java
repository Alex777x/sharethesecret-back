package pl.aliaksandrou.sharethesecretback.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.aliaksandrou.sharethesecretback.model.NoteData;
import pl.aliaksandrou.sharethesecretback.model.NoteRequest;
import pl.aliaksandrou.sharethesecretback.model.NoteResponse;
import pl.aliaksandrou.sharethesecretback.service.NoteService;

import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
@AllArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    public ResponseEntity<NoteResponse> createNote(
            @RequestParam(value = "content", required = false) String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("algorithm") String algorithm,
            @RequestParam("ttl") String ttl
    ) {
        NoteRequest request = new NoteRequest(content, file, algorithm, ttl);
        String id = noteService.saveNote(request);
        return ResponseEntity.ok(new NoteResponse(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNote(@PathVariable String id) {
        NoteData note = noteService.getNote(id);
        if (note == null) {
            return ResponseEntity.notFound().build();
        }

        if ("1-view".equals(note.getTtl())) {
            noteService.deleteNote(id);
        }

        if (note.isFile()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + note.getFilename() + "\"")
                    .contentType(MediaType.parseMediaType(note.getMimeType()))
                    .body(note.getEncryptedData());
        } else {
            String base64 = Base64.getEncoder().encodeToString(note.getEncryptedData());
            return ResponseEntity.ok(Map.of("content", base64));
        }
    }
}
