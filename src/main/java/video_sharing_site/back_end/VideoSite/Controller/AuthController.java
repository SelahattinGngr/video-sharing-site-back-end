package video_sharing_site.back_end.VideoSite.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import video_sharing_site.back_end.VideoSite.Dto.UserDTO;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Exception.BaseTokenExceptions;
import video_sharing_site.back_end.VideoSite.Exception.BaseUserExceptions;
import video_sharing_site.back_end.VideoSite.Exception.TokenExceptions.TokenInvalidException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserInvalidException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserNotFoundException;
import video_sharing_site.back_end.VideoSite.Service.AuthService;
import video_sharing_site.back_end.VideoSite.Shared.Config.LogConfig;

@RestController
@RequestMapping("/${video-site.server.api.key}/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private LogConfig logConfig;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO) request.getAttribute("userDto");
            if (user == null)
                throw new UserException();
            UsersEntity userEntity = authService.signup(user);
            logConfig.log(request.getMethod(), getClass().getName(), "User created successfully.", userEntity);
            return new ResponseEntity<>(Map.of("Success", "User created successfully."), HttpStatus.CREATED);
        } catch (UserException e) {
            return new BaseUserExceptions().exception("signing up.");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> login(HttpServletRequest request) {
        try {
            UserDTO user = (UserDTO) request.getAttribute("userDto");
            if (user == null)
                throw new UserException();
            UsersEntity userEntity = authService.signin(user);
            logConfig.log(request.getMethod(), getClass().getName(), "User signin in successfully.", userEntity);
            return new ResponseEntity<>(Map.of("Success", "User signin in successfully.", "RefreshToken",
                    userEntity.getRefreshToken(), "AccessToken", userEntity.getAccessToken()), HttpStatus.OK);
        } catch (UserException e) {
            return new BaseUserExceptions().exception("signign in.");
        } catch (UserInvalidException e) {
            return new BaseUserExceptions().invalidException();
        } catch (UserNotFoundException e) {
            return new BaseUserExceptions().notFoundException();
        } catch (Exception e) {
            return new BaseUserExceptions().exception("signing in.");
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<Map<String, Object>> signout(@RequestHeader("Authorization") String authorization) {
        String refreshToken = authorization.split(" ")[1];
        try {
            authService.signout(refreshToken);
            logConfig.log("POST", getClass().getName(), "User logged out successfully.", null);
            return new ResponseEntity<>(Map.of("Success", "User logged out successfully."), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new BaseUserExceptions().notFoundException();
        } catch (TokenInvalidException e) {
            return new BaseTokenExceptions().invalidException();
        } catch (UserException e) {
            return new BaseUserExceptions().exception("logging out.");
        } catch (UserInvalidException e) {
            return new BaseUserExceptions().invalidException();
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, Object>> postMethodName(@RequestHeader("Authorization") String authorization) {
        String refreshToken = authorization.split(" ")[1];
        try {
            Map<String, Object> response = authService.refreshToken(refreshToken);
            logConfig.log("POST", getClass().getName(), "Token refreshed successfully.", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new BaseUserExceptions().notFoundException();
        } catch (TokenInvalidException e) {
            return new BaseTokenExceptions().invalidException();
        } catch (UserException e) {
            return new BaseUserExceptions().exception("refreshing token.");
        }
    }

    // TODO: sifremi unuttum ve sifre degistirme islemleri eklenecek

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
