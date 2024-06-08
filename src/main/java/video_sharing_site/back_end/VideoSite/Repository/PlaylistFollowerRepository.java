package video_sharing_site.back_end.VideoSite.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import video_sharing_site.back_end.VideoSite.Entity.PlaylistFollowerEntity;
import video_sharing_site.back_end.VideoSite.Entity.PlaylistsEntity;

@Repository
public interface PlaylistFollowerRepository extends JpaRepository<PlaylistFollowerEntity, Long> {
    // playlist id'sine göre butun takipcileri gorme
    List<PlaylistFollowerEntity> findByPlaylistId(PlaylistsEntity playlistId);
}
