package pl.aliaksandrou.sharethesecretback.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@AllArgsConstructor
@Data
@Builder
public class NoteData {
    private byte[] encryptedData;
    private boolean isFile;
    private String filename;
    private String mimeType;
    private String ttl;
    private Instant createDate;
}
