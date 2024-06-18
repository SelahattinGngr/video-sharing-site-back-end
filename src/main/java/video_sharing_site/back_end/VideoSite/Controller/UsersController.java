package video_sharing_site.back_end.VideoSite.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import video_sharing_site.back_end.VideoSite.Exception.BaseUserExceptions;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserForbiddenException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserNotFoundException;
import video_sharing_site.back_end.VideoSite.Service.UsersService;

@RestController
@RequestMapping("/${video-site.server.api.key}/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getUsers(@RequestHeader("Authorization") String Authorization) {
        String token = Authorization.split(" ")[1];
        try {
            Map<String, Object> response = usersService.getUsers(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserForbiddenException e) {
            return new BaseUserExceptions().forbiddenException();
        }
    }

    @GetMapping("/{userName}")
    public ResponseEntity<Map<String, Object>> getUser(@RequestHeader("Authorization") String Authorization,
            @PathVariable("userName") String userName) {
        try {
            Map<String, Object> response = usersService.getUser(userName);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new BaseUserExceptions().notFoundException();
        }
    }
}
