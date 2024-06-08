package video_sharing_site.back_end.VideoSite.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import video_sharing_site.back_end.VideoSite.Entity.PlaylistsEntity;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;

@Repository
public interface PlaylistsRepository extends JpaRepository<PlaylistsEntity, Long> {
    // playlist url'sine göre playlist bulma
    PlaylistsEntity findByPlaylistUrl(String playlistUrl);

    // kullanıcı id'sine göre sahip olduğu playlistleri bulma
    List<PlaylistsEntity> findByUserId(UsersEntity userId);

    // herkese açık playlistleri bulma
    List<PlaylistsEntity> findByIsPublic(boolean isPublic);
}
