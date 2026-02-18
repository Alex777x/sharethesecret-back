package pl.aliaksandrou.sharethesecretback.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NoteResponse> createNote(
            @RequestPart(value = "content", required = false) String content,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @RequestPart("algorithm") String algorithm,
            @RequestPart("ttl") String ttl) {
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

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(Map.of("error", "File size exceeds the maximum allowed size of 2MB"));
    }
}
