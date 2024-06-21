package video_sharing_site.back_end.VideoSite.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import video_sharing_site.back_end.VideoSite.Entity.PlaylistVideosEntity;
import video_sharing_site.back_end.VideoSite.Entity.PlaylistsEntity;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserForbiddenException;
import video_sharing_site.back_end.VideoSite.Repository.PlaylistVideosRepository;
import video_sharing_site.back_end.VideoSite.Repository.PlaylistsRepository;
import video_sharing_site.back_end.VideoSite.Repository.UsersRepository;
import video_sharing_site.back_end.VideoSite.Shared.Services.jwt.TokenService;

@Service
public class PlaylistsService {

    @Autowired
    private PlaylistsRepository playlistsRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PlaylistVideosRepository playlistVideosRepository;

    @Autowired
    private UsersRepository usersRepository;

    public Map<String, Object> getPublicPlaylists(Pageable page) {
        List<PlaylistsEntity> playlists = playlistsRepository.findByStatus(true, page);
        List<Map<String, Object>> playlistList = new ArrayList<>();

        for (PlaylistsEntity playlist : playlists) {
            Map<String, Object> playlistData = new HashMap<>();
            playlistData.put("id", playlist.getId());
            playlistData.put("title", playlist.getTitle());
            playlistData.put("description", playlist.getDescription());
            playlistData.put("videoCount", playlist.getVideoCount());
            List<PlaylistVideosEntity> playlistVideos = playlistVideosRepository.findByPlaylistId(playlist);
            List<Map<String, Object>> playlistVideoData = new ArrayList<>();
            int video_size = 0;
            for (PlaylistVideosEntity playlistVideo : playlistVideos) {
                Map<String, Object> videoData = new HashMap<>();
                videoData.put("id", playlistVideo.getVideoId().getId());
                videoData.put("title", playlistVideo.getVideoId().getTitle());
                videoData.put("description", playlistVideo.getVideoId().getDescription());
                videoData.put("thumbnail", playlistVideo.getVideoId().getThumbnail());
                videoData.put("videoUrl", playlistVideo.getVideoId().getUrl());
                playlistVideoData.add(videoData);
                video_size++;
                if (video_size == 3) {
                    break;
                }
            }
            playlistData.put("videos", playlistVideoData);
            playlistList.add(playlistData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("playlists", playlistList);

        return response;
    }

    public Map<String, Object> getUserPlaylists(String Authorization, Pageable page) {
        String token = Authorization.split(" ")[1];
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new UserForbiddenException();
        }

        List<PlaylistsEntity> playlists = playlistsRepository.findByUserId(user, page);
        List<Map<String, Object>> playlistList = new ArrayList<>();

        for (PlaylistsEntity playlist : playlists) {
            Map<String, Object> playlistData = new HashMap<>();
            playlistData.put("id", playlist.getId());
            playlistData.put("title", playlist.getTitle());
            playlistData.put("description", playlist.getDescription());
            playlistData.put("videoCount", playlist.getVideoCount());
            List<PlaylistVideosEntity> playlistVideos = playlistVideosRepository.findByPlaylistId(playlist);
            List<Map<String, Object>> playlistVideoData = new ArrayList<>();
            int video_size = 0;
            for (PlaylistVideosEntity playlistVideo : playlistVideos) {
                Map<String, Object> videoData = new HashMap<>();
                videoData.put("id", playlistVideo.getVideoId().getId());
                videoData.put("title", playlistVideo.getVideoId().getTitle());
                videoData.put("description", playlistVideo.getVideoId().getDescription());
                videoData.put("thumbnail", playlistVideo.getVideoId().getThumbnail());
                videoData.put("videoUrl", playlistVideo.getVideoId().getUrl());
                playlistVideoData.add(videoData);
                video_size++;
                if (video_size == 3) {
                    break;
                }
            }
            playlistData.put("videos", playlistVideoData);
            playlistList.add(playlistData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("playlists", playlistList);

        return response;
    }

    public Map<String, Object> createPlaylist(String Authorization, Map<String, Object> playlist) {
        String token = Authorization.split(" ")[1];
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity user = usersRepository.findByEmail(email);

        String url;
        Random random = new Random();
        do {
            int nbr = random.nextInt(10000, 1000000);
            String tmpUrl = Integer.toHexString(nbr);
            PlaylistsEntity playlistUrl = playlistsRepository.findByPlaylistUrl(tmpUrl);
            if (playlistUrl == null) {
                url = tmpUrl;
                break;
            }
        } while (true);

        PlaylistsEntity newPlaylist = new PlaylistsEntity();
        newPlaylist.setTitle((String) playlist.get("title"));
        newPlaylist.setDescription((String) playlist.get("description"));
        newPlaylist.setUserId(user);
        newPlaylist.setPlaylistUrl(url);

        playlistsRepository.save(newPlaylist);
        return Map.of("message", "Playlist created successfully");
    }

    public Map<String, Object> updatePlaylist(String authorization, Long id, Map<String, Object> playlist) {
        String token = authorization.split(" ")[1];
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new UserForbiddenException();
        }

        PlaylistsEntity playlistEntity = playlistsRepository.findById(id).get();
        if (!playlistEntity.getUserId().getId().equals(user.getId())) {
            throw new UserForbiddenException();
        }
        String title = (String) playlist.get("title");
        String description = (String) playlist.get("description");
        Boolean status = (Boolean) playlist.get("status");

        playlistEntity.setTitle(Optional.ofNullable(title).orElse(playlistEntity.getTitle()));
        playlistEntity.setDescription(Optional.ofNullable(description).orElse(playlistEntity.getDescription()));
        playlistEntity.setStatus(Optional.ofNullable(status).orElse(playlistEntity.isStatus()));

        playlistsRepository.save(playlistEntity);
        return Map.of("message", playlist);
    }

    public Map<String, Object> getPlaylist(String authorization, Long id, Pageable pageable) {
        String token = authorization.split(" ")[1];
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new UserForbiddenException();
        }
        PlaylistsEntity playlist = playlistsRepository.findById(id).get();
        if (!playlist.getUserId().getId().equals(user.getId()) && !playlist.isStatus()) {
            throw new UserForbiddenException();
        }
        List<PlaylistVideosEntity> playlistVideos = playlistVideosRepository.findByPlaylistId(playlist);
        List<Map<String, Object>> playlistVideoData = new ArrayList<>();
        for (PlaylistVideosEntity playlistVideo : playlistVideos) {
            Map<String, Object> videoData = new HashMap<>();
            videoData.put("id", playlistVideo.getVideoId().getId());
            videoData.put("title", playlistVideo.getVideoId().getTitle());
            videoData.put("description", playlistVideo.getVideoId().getDescription());
            videoData.put("thumbnail", playlistVideo.getVideoId().getThumbnail());
            videoData.put("videoUrl", playlistVideo.getVideoId().getUrl());
            playlistVideoData.add(videoData);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", playlist.getId());
        response.put("title", playlist.getTitle());
        response.put("description", playlist.getDescription());
        response.put("videos", playlistVideoData);

        return response;
    }

    public Map<String, Object> followPlaylist(String authorization, Long id) {
        String token = authorization.split(" ")[1];
        String email = tokenService.getUserFromAccessToken(token);
        UsersEntity user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new UserForbiddenException();
        }
        PlaylistsEntity playlist = playlistsRepository.findById(id).get();
        if (playlist == null) {
            throw new UserForbiddenException();
        }
        if (playlist.getFollowers().contains(user)) {
            throw new UserForbiddenException();
        }
        if (!playlist.isStatus()) {
            throw new UserForbiddenException();
        }
        List<UsersEntity> followers = playlist.getFollowers();
        followers.add(user);
        playlist.setFollowers(followers);
        playlistsRepository.save(playlist);
        return Map.of("message", "Playlist followed successfully");
    }

}
