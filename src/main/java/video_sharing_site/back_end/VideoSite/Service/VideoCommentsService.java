package video_sharing_site.back_end.VideoSite.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Entity.VideoCommentsEntity;
import video_sharing_site.back_end.VideoSite.Entity.VideosEntity;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserForbiddenException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserNotFoundException;
import video_sharing_site.back_end.VideoSite.Exception.VideoExceptions.VideoCommentNotFound;
import video_sharing_site.back_end.VideoSite.Exception.VideoExceptions.VideoNotFoundException;
import video_sharing_site.back_end.VideoSite.Repository.UsersRepository;
import video_sharing_site.back_end.VideoSite.Repository.VideoCommentsRepository;
import video_sharing_site.back_end.VideoSite.Repository.VideosRepository;
import video_sharing_site.back_end.VideoSite.Shared.Services.jwt.TokenService;

@Service
public class VideoCommentsService {

    @Autowired
    private VideoCommentsRepository videoCommentsRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private VideosRepository videosRepository;

    @Autowired
    private UsersRepository usersRepository;

    public Map<String, Object> getVideoComments(Long id, Pageable pageable) {
        VideosEntity video = videosRepository.findById(id).get();
        if (video == null) {
            throw new VideoNotFoundException();
        }
        List<VideoCommentsEntity> videoComments = videoCommentsRepository.findByVideoIdAndStatus(video, true, pageable);
        if (videoComments == null) {
            throw new VideoNotFoundException();
        }

        List<Map<String, Object>> videoCommentsList = new ArrayList<>();
        for (VideoCommentsEntity videoComment : videoComments) {
            if (videoComment.getParentId() != null) {
                continue;
            }
            Map<String, Object> videoCommentData = new HashMap<>();
            videoCommentData.put("id", videoComment.getId());
            videoCommentData.put("content", videoComment.getContent());
            videoCommentData.put("likes", videoComment.getLikeCount());
            videoCommentData.put("date", videoComment.getUpdatedAt());
            videoCommentData.put("user", videoComment.getUserId().getUserName());
            videoCommentData.put("parrentId", videoComment.getParentId());
            Map<String, Object> childComments = new HashMap<>();
            List<VideoCommentsEntity> childCommentsList = videoCommentsRepository.findByParentIdAndStatus(videoComment,
                    true);
            for (VideoCommentsEntity childComment : childCommentsList) {
                Map<String, Object> childCommentData = new HashMap<>();
                childCommentData.put("id", childComment.getId());
                childCommentData.put("content", childComment.getContent());
                childCommentData.put("likes", childComment.getLikeCount());
                childCommentData.put("date", childComment.getUpdatedAt());
                childCommentData.put("user", childComment.getUserId().getUserName());
                childCommentData.put("parrentId", childComment.getParentId().getId());
                childComments.put(childComment.getId().toString(), childCommentData);
            }
            videoCommentData.put("childComments", childComments);
            videoCommentsList.add(videoCommentData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("comments", videoCommentsList);

        return response;
    }

    public Map<String, Object> likeVideoComment(String authorization, Long id) {
        VideoCommentsEntity videoComment = videoCommentsRepository.findById(id).get();
        if (videoComment == null) {
            throw new VideoCommentNotFound();
        }
        String email = tokenService.getUserFromAccessToken(authorization);
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }

        List<UsersEntity> likes = videoComment.getLikes();
        if (likes == null) {
            throw new NullPointerException();
        }

        if (likes.contains(user)) {
            likes.remove(user);
            videoComment.setLikes(likes);
            videoComment.setLikeCount(videoComment.getLikeCount() - 1);
            videoCommentsRepository.save(videoComment);
            return Map.of("message", "Comment unliked successfully");
        }

        likes.add(user);
        videoComment.setLikes(likes);
        videoComment.setLikeCount(videoComment.getLikeCount() + 1);
        videoCommentsRepository.save(videoComment);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Comment liked successfully");

        return response;
    }

    public Map<String, Object> updateVideoComment(String authorization, Long id, Map<String, Object> body) {
        String token = authorization.split(" ")[1];
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }

        VideoCommentsEntity videoComment = videoCommentsRepository.findById(id).get();
        if (videoComment == null) {
            throw new VideoCommentNotFound();
        }

        if (videoComment.getUserId().getId() != user.getId()) {
            throw new UserForbiddenException();
        }

        videoComment.setContent(body.get("content").toString());
        videoCommentsRepository.save(videoComment);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Comment updated successfully");

        return response;
    }

    public Map<String, Object> deleteVideoComment(String authorization, Long id) {
        String token = authorization.split(" ")[1];
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }

        VideoCommentsEntity videoComment = videoCommentsRepository.findById(id).get();
        if (videoComment == null) {
            throw new VideoCommentNotFound();
        }

        if (videoComment.getUserId().getId() != user.getId()) {
            throw new UserForbiddenException();
        }

        videoComment.setStatus(false);
        videoCommentsRepository.save(videoComment);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Comment deleted successfully");

        return response;
    }

    public Map<String, Object> postVideoComment(String authorization, Map<String, Object> body, Long id) {
        String token = authorization.split(" ")[1];
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }

        VideosEntity video = videosRepository.findById(id).get();
        if (video == null) {
            throw new VideoNotFoundException();
        }
        String parentId = body.get("parentId").toString();
        Long parentIdLong = Long.parseLong(parentId);
        VideoCommentsEntity tmp;
        if (parentIdLong > 0) {
            tmp = videoCommentsRepository.findById(parentIdLong).get();
            if (tmp == null) {
                throw new VideoCommentNotFound();
            }
        } else {
            tmp = null;
        }
        VideoCommentsEntity videoComment = new VideoCommentsEntity();
        videoComment.setUserId(user);
        videoComment.setVideoId(video);
        videoComment.setParentId(tmp);
        videoComment.setContent(body.get("content").toString());
        videoCommentsRepository.save(videoComment);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Comment posted successfully");

        return response;
    }

    public Map<String, Object> getVideoCommentLikes(Long id, Pageable pageable) {
        VideoCommentsEntity videoComment = videoCommentsRepository.findById(id).get();
        if (videoComment == null) {
            throw new VideoCommentNotFound();
        }
        List<UsersEntity> likes = videoComment.getLikes();
        if (likes == null) {
            throw new NullPointerException();
        }

        List<Map<String, Object>> likesList = new ArrayList<>();
        for (UsersEntity like : likes) {
            Map<String, Object> likeData = new HashMap<>();
            likeData.put("id", like.getId());
            likeData.put("username", like.getUserName());
            likesList.add(likeData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("likes", likesList);

        return response;
    }

}
