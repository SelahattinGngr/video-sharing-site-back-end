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

import video_sharing_site.back_end.VideoSite.Entity.VideosEntity;
import video_sharing_site.back_end.VideoSite.Exception.BaseUserExceptions;
import video_sharing_site.back_end.VideoSite.Exception.BaseVideoExceptions;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserForbiddenException;
import video_sharing_site.back_end.VideoSite.Exception.VideoExceptions.VideoErrorException;
import video_sharing_site.back_end.VideoSite.Exception.VideoExceptions.VideoNotFoundException;
import video_sharing_site.back_end.VideoSite.Exception.VideoExceptions.VideoUrlException;
import video_sharing_site.back_end.VideoSite.Service.VideosService;
import video_sharing_site.back_end.VideoSite.Shared.Config.LogConfig;

@RestController
@RequestMapping("/${video-site.server.api.key}/videos")
@SuppressWarnings("null")
public class VideosController {

    @Autowired
    private VideosService videosService;

    @Autowired
    private LogConfig logConfig;

    // TODO: @RequestParam(defaultValue = "") string search query ekle

    @GetMapping
    public ResponseEntity<Map<String, Object>> getVideos(
            @RequestHeader(value = "Authorization", required = false) String Authorization,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            if (page <= 0) {
                throw new VideoErrorException();
            }
            Pageable pageable = PageRequest.of(page - 1, size);
            response = videosService.getVideos(pageable);
            mesagge = "getting videos by page : " + page;
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (VideoErrorException e) {
            responseEntity = new BaseVideoExceptions().errorException("getting videos");
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "GET", getClass().getName(), mesagge);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getVideo(
            @RequestHeader(value = "Authorization", required = false) String Authorization,
            @PathVariable Long id) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            if (id < 1) {
                throw new VideoErrorException();
            }
            response = videosService.getVideo(id);
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
            mesagge = "getting video by id : " + id;
        } catch (VideoErrorException e) {
            responseEntity = new BaseVideoExceptions().errorException("getting videos by id : " + id.toString());
            mesagge = responseEntity.getBody().get("error").toString();
        } catch (VideoNotFoundException e) {
            responseEntity = new BaseVideoExceptions().notFoundException(id);
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "GET", getClass().getName(), mesagge);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @GetMapping("/like/{id}")
    public ResponseEntity<Map<String, Object>> likeVideo(@RequestHeader("Authorization") String Authorization,
            @PathVariable Long id) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            response = videosService.likeVideo(id, Authorization);
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
            mesagge = "liking video by id : " + id;
        } catch (VideoErrorException e) {
            responseEntity = new BaseVideoExceptions().errorException("liking video by id : " + id.toString());
            mesagge = responseEntity.getBody().get("error").toString();
        } catch (VideoNotFoundException e) {
            responseEntity = new BaseVideoExceptions().notFoundException(id);
            mesagge = responseEntity.getBody().get("error").toString();
        } catch (UserForbiddenException e) {
            responseEntity = new BaseUserExceptions().forbiddenException();
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "GET", getClass().getName(), mesagge);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> uploadVideo(@RequestHeader("Authorization") String Authorization,
            @RequestBody VideosEntity video) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            response = videosService.uploadVideo(video, Authorization);
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
            mesagge = "uploading video";
        } catch (VideoErrorException e) {
            responseEntity = new BaseVideoExceptions().errorException("uploading video");
            mesagge = responseEntity.getBody().get("error").toString();
        } catch (UserForbiddenException e) {
            responseEntity = new BaseUserExceptions().forbiddenException();
            mesagge = responseEntity.getBody().get("error").toString();
        } catch (VideoUrlException e) {
            responseEntity = new BaseVideoExceptions().urlException();
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "POST", getClass().getName(), mesagge);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateVideo(@RequestHeader("Authorization") String Authorization,
            @PathVariable Long id, @RequestBody VideosEntity video) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            response = videosService.updateVideo(id, video, Authorization);
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
            mesagge = "updating video by id : " + id;
        } catch (VideoErrorException e) {
            responseEntity = new BaseVideoExceptions().errorException("updating video by id : " + id.toString());
            mesagge = responseEntity.getBody().get("error").toString();
        } catch (VideoNotFoundException e) {
            responseEntity = new BaseVideoExceptions().notFoundException(id);
            mesagge = responseEntity.getBody().get("error").toString();
        } catch (UserForbiddenException e) {
            responseEntity = new BaseUserExceptions().forbiddenException();
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "PUT", getClass().getName(), mesagge);
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }
}
