package pl.aliaksandrou.sharethesecretback.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Data
@Builder
public class NoteRequest {
    private String content;
    private MultipartFile file;
    private String algorithm;
    private String ttl;
}
