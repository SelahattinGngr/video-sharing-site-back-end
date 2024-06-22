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
import video_sharing_site.back_end.VideoSite.Service.FollowersService;

// TODO: takip edilen kullanıcıları da getir

@RestController
@RequestMapping("/${video-site.server.api.key}/followers")
public class FollowersController {

    @Autowired
    private FollowersService followersService;

    @GetMapping("/followers/{userName}")
    public ResponseEntity<Map<String, Object>> getFollowers(@RequestHeader("Authorization") String Authorization,
            @PathVariable("userName") String userName) {
        String token = Authorization.split(" ")[1];
        try {
            Map<String, Object> response = followersService.getFollowers(token, userName);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserForbiddenException e) {
            return new BaseUserExceptions().forbiddenException();
        } catch (UserNotFoundException e) {
            return new BaseUserExceptions().notFoundException();
        }
    }

    @GetMapping("/follow/{userName}")
    public ResponseEntity<Map<String, Object>> followUser(@RequestHeader("Authorization") String Authorization,
            @PathVariable("userName") String userName) {
        String token = Authorization.split(" ")[1];
        try {
            Map<String, Object> response = followersService.followUser(token, userName);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserForbiddenException e) {
            return new BaseUserExceptions().forbiddenException();
        } catch (UserNotFoundException e) {
            return new BaseUserExceptions().notFoundException();
        }
    }

    @GetMapping("/unfollow/{userName}")
    public ResponseEntity<Map<String, Object>> unfollowUser(@RequestHeader("Authorization") String Authorization,
            @PathVariable("userName") String userName) {
        String token = Authorization.split(" ")[1];
        try {
            Map<String, Object> response = followersService.unfollowUser(token, userName);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserForbiddenException e) {
            return new BaseUserExceptions().forbiddenException();
        } catch (UserNotFoundException e) {
            return new BaseUserExceptions().notFoundException();
        }
    }
}
