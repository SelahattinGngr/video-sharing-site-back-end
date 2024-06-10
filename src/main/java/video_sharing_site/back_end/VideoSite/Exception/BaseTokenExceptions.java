package video_sharing_site.back_end.VideoSite.Exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import video_sharing_site.back_end.VideoSite.Exception.TokenExceptions.TokenInvalidException;

public class BaseTokenExceptions extends RuntimeException{
    
    @ExceptionHandler(TokenInvalidException.class)
    public ResponseEntity<Map<String, Object>> invalidException() {
        return new ResponseEntity<>(Map.of("error", "Invalid token"), HttpStatus.UNAUTHORIZED);
    }
}
