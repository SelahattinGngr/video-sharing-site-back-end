package video_sharing_site.back_end.VideoSite.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import video_sharing_site.back_end.VideoSite.Entity.PlaylistVideosEntity;
import video_sharing_site.back_end.VideoSite.Entity.PlaylistsEntity;
import video_sharing_site.back_end.VideoSite.Entity.VideosEntity;

import java.util.List;

@Repository
public interface PlaylistVideosRepository extends JpaRepository<PlaylistVideosEntity, Long> {
    // video id'sine göre playlist bulma
    PlaylistVideosEntity findByVideoIdId(VideosEntity videoId);

    // bunu niye yazdım unuttum :(
    PlaylistVideosEntity findByPlaylistId(PlaylistsEntity playlistId);

    // bir videonun hangi playlistlerde olduğunu bulma
    List<PlaylistVideosEntity> findByVideoId(VideosEntity videoId);
}
