package video_sharing_site.back_end.VideoSite.Exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import video_sharing_site.back_end.VideoSite.Exception.CategoryExceptions.CategoryException;

@RestControllerAdvice
public class BaseCategoryExceptions extends RuntimeException {
    
    @ExceptionHandler(CategoryException.class)
    public ResponseEntity<Map<String, Object>> categoryException() {
        return new ResponseEntity<>(Map.of("Error", "category already exists."), HttpStatus.CONFLICT);
    }
}
