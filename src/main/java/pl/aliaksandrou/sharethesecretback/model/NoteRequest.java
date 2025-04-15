package pl.aliaksandrou.sharethesecretback.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class NoteRequest {
    private String content;
    private MultipartFile file;
    private String algorithm;
    private String ttl;

    public NoteRequest(String content, MultipartFile file, String algorithm, String ttl) {
        // If content is null and file is not null, use the file's content
        if (content == null && file != null) {
            this.content = null; // We'll handle the file content separately
        } else {
            this.content = content;
        }
        this.file = file;
        this.algorithm = algorithm;
        this.ttl = ttl;
    }
}
