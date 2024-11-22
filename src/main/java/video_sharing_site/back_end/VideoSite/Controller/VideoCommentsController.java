package video_sharing_site.back_end.VideoSite.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserNotFoundException;
import video_sharing_site.back_end.VideoSite.Exception.VideoExceptions.VideoErrorException;
import video_sharing_site.back_end.VideoSite.Exception.VideoExceptions.VideoNotFoundException;
import video_sharing_site.back_end.VideoSite.Service.VideoCommentsService;
import video_sharing_site.back_end.VideoSite.Shared.Config.LogConfig;

@RestController
@RequestMapping("/${video-site.server.api.key}/video-comments")
@SuppressWarnings("null")
public class VideoCommentsController {

    @Autowired
    private VideoCommentsService videoCommentsService;

    @Autowired
    private LogConfig logConfig;

    // videoda bulunan yorumları getirir
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getVideoComments(@RequestHeader("Authorization") String Authorization,
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,
            @PathVariable("id") Long id) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            if (page <= 0) {
                throw new VideoErrorException();
            }
            Pageable pageable = PageRequest.of(page - 1, size);
            response = videoCommentsService.getVideoComments(id, pageable);
            mesagge = "getting video comments by video id : " + id;
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (VideoErrorException e) {
            responseEntity = new BaseVideoExceptions().errorException("getting videos");
            mesagge = responseEntity.getBody().get("error").toString();
        } catch (VideoNotFoundException e) {
            responseEntity = new BaseVideoExceptions().notFoundException(id);
            mesagge = responseEntity.getBody().get("error").toString();
        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = new BaseVideoExceptions().errorException("getting video comments");
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "GET", getClass().getName(), mesagge);
        return responseEntity;
    }

    // yorumdaki begenileri getirir
    @GetMapping("/likes/{id}")
    public ResponseEntity<Map<String, Object>> getVideoCommentLikes(
            @RequestHeader("Authorization") String Authorization,
            @PathVariable("id") Long id, @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            Pageable pageable = PageRequest.of(page - 1, size);
            response = videoCommentsService.getVideoCommentLikes(id, pageable);
            mesagge = "getting video comment likes by video comment id : " + id;
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            responseEntity = new BaseVideoExceptions().errorException("getting video comment likes");
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "GET", getClass().getName(), mesagge);
        return responseEntity;
    }

    // yorumu beğenir
    @GetMapping("/like/{id}")
    public ResponseEntity<Map<String, Object>> likeVideoComment(@RequestHeader("Authorization") String Authorization,
            @PathVariable("id") Long id) {
        String token = Authorization.split(" ")[1];
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            response = videoCommentsService.likeVideoComment(token, id);
            mesagge = response.get("message").toString() + " - video comment id : " + id;
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            responseEntity = new BaseVideoExceptions().errorException("liking video comment");
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "GET", getClass().getName(), mesagge);
        return responseEntity;
    }

    // yorum ekler
    @PostMapping("/{id}")
    public ResponseEntity<Map<String, Object>> postVideoComment(@RequestHeader("Authorization") String Authorization,
            @PathVariable("id") Long id, @RequestBody Map<String, Object> body) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            response = videoCommentsService.postVideoComment(Authorization, body, id);
            mesagge = "posting video comment";
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            responseEntity = new BaseUserExceptions().notFoundException();
            mesagge = responseEntity.getBody().get("error").toString();
        } catch (VideoNotFoundException e) {
            responseEntity = new BaseVideoExceptions().notFoundException(id);
            mesagge = responseEntity.getBody().get("error").toString();
        } catch (Exception e) {
            responseEntity = new BaseVideoExceptions().errorException("posting video comment");
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "POST", getClass().getName(), mesagge);
        return responseEntity;
    }

    // yorumu günceller
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateVideoComment(@RequestHeader("Authorization") String Authorization,
            @PathVariable("id") Long id, @RequestBody Map<String, Object> body) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            response = videoCommentsService.updateVideoComment(Authorization, id, body);
            mesagge = "updating video comment by video comment id : " + id;
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            responseEntity = new BaseVideoExceptions().errorException("updating video comment");
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "PUT", getClass().getName(), mesagge);
        return responseEntity;
    }

    // yorumu siler - statusu false yapar
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteVideoComment(@RequestHeader("Authorization") String Authorization,
            @PathVariable("id") Long id) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            response = videoCommentsService.deleteVideoComment(Authorization, id);
            mesagge = "deleting video comment by video comment id : " + id;
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            responseEntity = new BaseVideoExceptions().errorException("deleting video comment");
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "DELETE", getClass().getName(), mesagge);
        return responseEntity;
    }
}
