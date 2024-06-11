package video_sharing_site.back_end.VideoSite.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import video_sharing_site.back_end.VideoSite.Dto.UserDto;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Exception.BaseTokenExceptions;
import video_sharing_site.back_end.VideoSite.Exception.BaseUserExceptions;
import video_sharing_site.back_end.VideoSite.Exception.TokenExceptions.TokenInvalidException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserInvalidException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserNotFoundException;
import video_sharing_site.back_end.VideoSite.Service.AuthService;

@RestController
@RequestMapping("/${video-site.server.api.key}/auth")
public class AuthController {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(HttpServletRequest request) {
        try {
            UserDto user = (UserDto) request.getAttribute("userDto");
            if (user == null)
                throw new UserException();
            authService.signup(user);
            return new ResponseEntity<>(Map.of("Success", "User created successfully."), HttpStatus.CREATED);
        } catch (UserException e) {
            return new BaseUserExceptions().exception("signing up.");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UsersEntity user) {
        try {
            return new ResponseEntity<>(Map.of("Success", "User logged in successfully."), HttpStatus.OK);
        } catch (UserInvalidException e) {
            return new BaseUserExceptions().invalidException();
        } catch (UserNotFoundException e) {
            return new BaseUserExceptions().notFoundException();
        } catch (UserException e) {
            return new BaseUserExceptions().exception("signign in.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String authorization,
            @RequestBody UsersEntity user) {
        try {
            return new ResponseEntity<>(Map.of("Success", "User logged out successfully."), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new BaseUserExceptions().notFoundException();
        } catch (TokenInvalidException e) {
            return new BaseTokenExceptions().invalidException();
        } catch (UserException e) {
            return new BaseUserExceptions().exception("logging out.");
        }
    }

    // @PostMapping("/forgot-password")
    // public ResponseEntity<Map<String, Object>> forgotPassword() {
    // try {
    // } catch (Exception e) {
    // }
    // }

    // @PostMapping("/reset-password")
    // public ResponseEntity<Map<String, Object>> resetPassword() {
    // try {
    // } catch (Exception e) {
    // }
    // }

}
