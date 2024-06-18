package video_sharing_site.back_end.VideoSite.Exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.*;

@RestControllerAdvice
public class BaseUserExceptions extends RuntimeException {

    @ExceptionHandler(UserEmailException.class)
    public ResponseEntity<Map<String, Object>> emailException() {
        return new ResponseEntity<>(Map.of("Error", "E-mail already exists."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserUsernameException.class)
    public ResponseEntity<Map<String, Object>> usernameException() {
        return new ResponseEntity<>(Map.of("Error", "Username already exists."), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserInvalidException.class)
    public ResponseEntity<Map<String, Object>> invalidException() {
        return new ResponseEntity<>(Map.of("Error", "Invalid E-mail/Username or password."), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, Object>> exception(String message) {
        return new ResponseEntity<>(Map.of("Error", "An error occurred while " + message),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> notFoundException() {
        return new ResponseEntity<>(Map.of("Error", "User not found."), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserEmailValidateException.class)
    public ResponseEntity<Map<String, Object>> emailValidateException() {
        return new ResponseEntity<>(Map.of("Error", "Invalid E-mail."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserUsernameValidateException.class)
    public ResponseEntity<Map<String, Object>> usernameValidateException() {
        return new ResponseEntity<>(Map.of("Error", "Invalid Username."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserPasswordValidateException.class)
    public ResponseEntity<Map<String, Object>> passwordValidateException() {
        return new ResponseEntity<>(Map.of("Error", "Invalid Password."), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserForbiddenException.class)
    public ResponseEntity<Map<String, Object>> forbiddenException() {
        return new ResponseEntity<>(Map.of("error", "Forbidden action"), HttpStatus.FORBIDDEN);
    }
}
