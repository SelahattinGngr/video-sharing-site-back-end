package video_sharing_site.back_end.VideoSite.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import video_sharing_site.back_end.VideoSite.Exception.BaseUserExceptions;
import video_sharing_site.back_end.VideoSite.Exception.BaseVideoExceptions;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserForbiddenException;
import video_sharing_site.back_end.VideoSite.Exception.VideoExceptions.VideoErrorException;
import video_sharing_site.back_end.VideoSite.Service.PlaylistsService;
import video_sharing_site.back_end.VideoSite.Shared.Config.LogConfig;



@RestController
@RequestMapping("/${video-site.server.api.key}/playlists")
@SuppressWarnings("null")
public class PlaylistsController {

    @Autowired
    private PlaylistsService playlistsService;

    @Autowired
    private LogConfig logConfig;

    // butun public playlistleri getirir
    @GetMapping("/public")
    public ResponseEntity<Map<String, Object>> getPublicPlaylists(@RequestHeader("Authorization") String Authorization,
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            if (page <= 0) {
                throw new VideoErrorException();
            }
            Pageable pageable = PageRequest.of(page - 1, size);
            response = playlistsService.getPublicPlaylists(pageable);
            mesagge = "getting playlists by page : " + page;
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (VideoErrorException e) {
            responseEntity = new BaseVideoExceptions().errorException("getting videos");
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "GET", getClass().getName(), mesagge);
        return responseEntity;
    }

    @GetMapping("/user-playlists")
    public ResponseEntity<Map<String, Object>> getMethodName(@RequestHeader("Authorization") String Authorization,
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            if (page <= 0) {
                throw new VideoErrorException();
            }
            Pageable pageable = PageRequest.of(page - 1, size);
            response = playlistsService.getUserPlaylists(Authorization, pageable);
            mesagge = "getting playlists by page : " + page;
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (VideoErrorException e) {
            responseEntity = new BaseVideoExceptions().errorException("getting videos");
            mesagge = responseEntity.getBody().get("error").toString();
        } catch (Exception e) {
        }
        logConfig.token(Authorization, "GET", getClass().getName(), mesagge);
        return responseEntity;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPlaylist(@RequestHeader("Authorization") String Authorization,
            @PathVariable Long id, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            response = playlistsService.getPlaylist(Authorization, id, pageable);
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            responseEntity = new BaseVideoExceptions().errorException("getting playlist");
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "GET", getClass().getName(), mesagge);
        return responseEntity;
    }

    @GetMapping("/follow/{id}")
    public ResponseEntity<Map<String, Object>> followPlaylist(@RequestHeader("Authorization") String Authorization,
            @PathVariable Long id) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            response = playlistsService.followPlaylist(Authorization, id);
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            responseEntity = new BaseVideoExceptions().errorException("following playlist");
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "GET", getClass().getName(), mesagge);
        return responseEntity;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createPlaylist(@RequestHeader("Authorization") String Authorization,
            @RequestBody Map<String, Object> playlist) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            response = playlistsService.createPlaylist(Authorization, playlist);
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
        }
        logConfig.token(Authorization, "POST", getClass().getName(), mesagge);
        return responseEntity;
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePlaylist(@RequestHeader("Authorization") String Authorization,
            @PathVariable Long id, @RequestBody Map<String, Object> playlist) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            response = playlistsService.updatePlaylist(Authorization, id, playlist);
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserForbiddenException e) {
            responseEntity = new BaseUserExceptions().forbiddenException();
            mesagge = responseEntity.getBody().get("error").toString();
        
        } catch (Exception e) {
            mesagge = e.getMessage();
            responseEntity = new BaseVideoExceptions().errorException("updating playlist");
        }
        logConfig.token(Authorization, "PUT", getClass().getName(), mesagge);
        return responseEntity;
    }
}
