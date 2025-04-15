package pl.aliaksandrou.sharethesecretback.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class NoteResponse {
    private String id;
}
