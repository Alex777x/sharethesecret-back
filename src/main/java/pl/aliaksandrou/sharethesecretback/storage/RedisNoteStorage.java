package pl.aliaksandrou.sharethesecretback.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import pl.aliaksandrou.sharethesecretback.model.NoteData;
import pl.aliaksandrou.sharethesecretback.model.NoteRequest;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
public class RedisNoteStorage implements NoteStorage {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public RedisNoteStorage(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void save(String id, NoteRequest request) {
        try {
            byte[] data;
            boolean isFile = false;
            String filename = null;
            String mimeType = "application/octet-stream";

            if (request.getFile() != null && !request.getFile().isEmpty()) {
                data = request.getFile().getBytes();
                isFile = true;
                filename = request.getFile().getOriginalFilename();
                mimeType = request.getFile().getContentType();
            } else if (request.getContent() != null) {
                data = request.getContent().getBytes();
                mimeType = "text/plain";
            } else {
                throw new IllegalArgumentException("Content or file must be provided.");
            }

            NoteData noteData = new NoteData(
                    data,
                    isFile,
                    filename,
                    mimeType,
                    request.getTtl(),
                    Instant.now()
            );

            String json = objectMapper.writeValueAsString(noteData);
            redisTemplate.opsForValue().set(id, json);

            // TTL setup
            long seconds = switch (request.getTtl()) {
                case "1h" -> 3600L;
                case "24h" -> 86400L;
                default -> 0L; // 1-view — удаляем вручную
            };

            if (seconds > 0) {
                redisTemplate.expire(id, seconds, TimeUnit.SECONDS);
            }

        } catch (IOException e) {
            throw new RuntimeException("Redis storage failed", e);
        }
    }

    @Override
    public NoteData get(String id) {
        String json = redisTemplate.opsForValue().get(id);
        if (json == null) return null;

        try {
            return objectMapper.readValue(json, NoteData.class);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void delete(String id) {
        redisTemplate.delete(id);
    }
}
