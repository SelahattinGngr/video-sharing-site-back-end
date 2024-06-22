package video_sharing_site.back_end.VideoSite.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import video_sharing_site.back_end.VideoSite.Entity.CategoriesEntity;
import video_sharing_site.back_end.VideoSite.Exception.BaseUserExceptions;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserForbiddenException;
import video_sharing_site.back_end.VideoSite.Service.CategoriesService;
import video_sharing_site.back_end.VideoSite.Shared.Config.LogConfig;

// TODO: guncelleme islemi eklenecek

@RestController
@RequestMapping("/${video-site.server.api.key}/categories")
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @Autowired
    private LogConfig logConfig;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getMethodName(@RequestHeader("Authorization") String Authorization) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            response = categoriesService.getCategories();
            mesagge = "getting categories";
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
        }
        logConfig.token(Authorization, "GET", getClass().getName(), mesagge);
        return responseEntity;
    }

    // TODO: post put islemlerindeki entityleri map yap
    @PostMapping
    @SuppressWarnings("null")
    public ResponseEntity<Map<String, Object>> newCategory(@RequestHeader("Authorization") String Authorization,
            @RequestBody CategoriesEntity categories) {
        ResponseEntity<Map<String, Object>> responseEntity = null;
        Map<String, Object> response = null;
        String mesagge = null;
        try {
            response = categoriesService.newCategory(Authorization, categories);
            mesagge = "creating new category";
            responseEntity = new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserForbiddenException e) {
            responseEntity = new BaseUserExceptions().forbiddenException();
            mesagge = responseEntity.getBody().get("error").toString();
        } catch (NullPointerException e) {
            responseEntity = new ResponseEntity<>(Map.of("error", "null argument"), HttpStatus.BAD_REQUEST);
            mesagge = responseEntity.getBody().get("error").toString();
        }
        logConfig.token(Authorization, "POST", getClass().getName(), mesagge);
        return responseEntity;
    }
}
