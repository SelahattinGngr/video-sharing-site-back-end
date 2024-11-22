package video_sharing_site.back_end.VideoSite.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import video_sharing_site.back_end.VideoSite.Entity.CategoriesEntity;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Entity.VideosEntity;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserForbiddenException;
import video_sharing_site.back_end.VideoSite.Exception.VideoExceptions.VideoErrorException;
import video_sharing_site.back_end.VideoSite.Exception.VideoExceptions.VideoNotFoundException;
import video_sharing_site.back_end.VideoSite.Exception.VideoExceptions.VideoUrlException;
import video_sharing_site.back_end.VideoSite.Repository.CategoriesRepository;
import video_sharing_site.back_end.VideoSite.Repository.UsersRepository;
import video_sharing_site.back_end.VideoSite.Repository.VideosRepository;
import video_sharing_site.back_end.VideoSite.Shared.Services.jwt.TokenService;

@Service
public class VideosService {

    @Autowired
    private VideosRepository videosRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CategoriesRepository categoriesRepository;

    public Map<String, Object> getVideos(Pageable page) {
        // Retrieve videos with status set to true
        List<VideosEntity> videos = videosRepository.findByStatus(true, page);

        // Create a list to store video data
        List<Map<String, Object>> videoList = new ArrayList<>();

        // Iterate over the videos and extract the required data
        for (VideosEntity video : videos) {
            Map<String, Object> videoData = new HashMap<>();
            videoData.put("id", video.getId());
            videoData.put("title", video.getTitle());
            videoData.put("views", video.getViews());
            videoData.put("thumbnail", video.getThumbnail());
            videoList.add(videoData);
        }

        // Create a response map and add the video list
        Map<String, Object> response = new HashMap<>();
        response.put("videos", videoList);

        return response;
    }

	public Map<String, Object> getVideo(Long id) {
		Optional<VideosEntity> video = videosRepository.findById(id);
        if (video.isEmpty()) {
            throw new VideoNotFoundException();
        }
        if (video.get().isStatus() == false) {
            throw new VideoNotFoundException();
        }
        Map<String, Object> uploadedUser = new HashMap<>();
        uploadedUser.put("uploaded_username", video.get().getUploadedUserId().getUserName());
        uploadedUser.put("uploaded_followers_count", video.get().getUploadedUserId().getFollowersCount());

        System.out.println(video.get().getViews());
        video.get().setViews(video.get().getViews() + 1);
        System.out.println(video.get().getViews());
        videosRepository.save(video.get());
        Map<String, Object> videoData = new HashMap<>();
        videoData.put("id", video.get().getId());
        videoData.put("title", video.get().getTitle());
        videoData.put("url", video.get().getUrl());
        videoData.put("description", video.get().getDescription());
        videoData.put("status", video.get().isStatus());
        videoData.put("views", video.get().getViews());
        videoData.put("like_count", video.get().getLikeCount());
        videoData.put("dislike_count", video.get().getDislikeCount());
        videoData.put("thumbnail", video.get().getThumbnail());
        videoData.put("uploaded_user", uploadedUser);
        Map<String, Object> response = new HashMap<>();
        response.put("video", videoData);
        return response;
	}

    public Map<String, Object> uploadVideo(VideosEntity video, String Authorization) {
        String token = Authorization.split(" ")[1];
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null || !user.isRole()) {
            throw new UserForbiddenException();
        }
        if (video.getTitle() == null || video.getDescription() == null || video.getUrl() == null || video.getThumbnail() == null) {
            throw new VideoErrorException();
        }
        VideosEntity exVideo = videosRepository.findByUrl(video.getUrl());
        if (exVideo != null) {
            throw new VideoUrlException();
        }
        video.setUploadedUserId(user);
        List<CategoriesEntity> categories = new ArrayList<>();
        for (CategoriesEntity category : video.getCategories()) {
            CategoriesEntity exCategory = categoriesRepository.findById(category.getId());
            if (exCategory != null) {
                categories.add(exCategory);
            }
        }
        videosRepository.save(video);
        categoriesRepository.saveAll(categories);
        return Map.of("message", "video uploaded successfully");
    }

    public Map<String, Object> updateVideo(Long id, VideosEntity video, String authorization) {
        String token = authorization.split(" ")[1];
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null || !user.isRole()) {
            throw new UserForbiddenException();
        }
        Optional<VideosEntity> exVideo = videosRepository.findById(id);
        if (exVideo.isEmpty()) {
            throw new VideoNotFoundException();
        }
        VideosEntity exVideoUrl = videosRepository.findByUrl(video.getUrl());
        if (exVideoUrl != null && exVideoUrl.getId() != id) {
            throw new VideoUrlException();
        }
        exVideo.get().setTitle(Optional.ofNullable(video.getTitle()).orElse(exVideo.get().getTitle()));
        exVideo.get().setDescription(Optional.ofNullable(video.getDescription()).orElse(exVideo.get().getDescription()));
        exVideo.get().setUrl(Optional.ofNullable(video.getUrl()).orElse(exVideo.get().getUrl()));
        exVideo.get().setThumbnail(Optional.ofNullable(video.getThumbnail()).orElse(exVideo.get().getThumbnail()));
        List<CategoriesEntity> categories = new ArrayList<>();
        for (CategoriesEntity category : video.getCategories()) {
            CategoriesEntity exCategory = categoriesRepository.findById(category.getId());
            if (exCategory != null) {
                categories.add(exCategory);
            }
        }
        exVideo.get().setCategories(categories);
        videosRepository.save(exVideo.get());
        categoriesRepository.saveAll(categories);
        return Map.of("message", "video updated successfully");
    }

    public Map<String, Object> likeVideo(Long id, String authorization) {
        String token = authorization.split(" ")[1];
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new UserForbiddenException();
        }
        Optional<VideosEntity> video = videosRepository.findById(id);
        if (video.isEmpty()) {
            throw new VideoNotFoundException();
        }
        if (video.get().isStatus() == false) {
            throw new VideoNotFoundException();
        }
        List<UsersEntity> likes = video.get().getLikes();
        if (likes.contains(user)) {
            likes.remove(user);
            video.get().setLikes(likes);
            video.get().setLikeCount(video.get().getLikeCount() - 1);
            videosRepository.save(video.get());
            return Map.of("message", "video unliked successfully");
        }
        likes.add(user);
        video.get().setLikes(likes);
        video.get().setLikeCount(video.get().getLikeCount() + 1);
        videosRepository.save(video.get());
        return Map.of("message", "video liked successfully");
    }
}
