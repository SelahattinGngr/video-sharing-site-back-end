package video_sharing_site.back_end.VideoSite.Exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import video_sharing_site.back_end.VideoSite.Exception.PlaylistExceptions.PlaylistNotFoundException;
import video_sharing_site.back_end.VideoSite.Exception.VideoExceptions.*;

@RestControllerAdvice
public class BaseVideoExceptions extends RuntimeException {

    @ExceptionHandler(VideoNotFoundException.class)
    public ResponseEntity<Map<String, Object>> notFoundException(Long id) {
        return new ResponseEntity<>(Map.of("error", "video not found by id : " + id.toString()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(VideoErrorException.class)
    public ResponseEntity<Map<String, Object>> errorException(String message) {
        return new ResponseEntity<>(Map.of("error", "An error occurred while " + message),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VideoUrlException.class)
    public ResponseEntity<Map<String, Object>> urlException() {
        return new ResponseEntity<>(Map.of("error", "url already exists."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(VideoCommentNotFound.class)
    public ResponseEntity<Map<String, Object>> commentNotFoundException(Long id) {
        return new ResponseEntity<>(Map.of("error", "comment not found by id : " + id.toString()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PlaylistNotFoundException.class)
    public ResponseEntity<Map<String, Object>> playlistNotFoundException() {
        return new ResponseEntity<>(Map.of("error", "playlist not found"), HttpStatus.NOT_FOUND);
    }
}
