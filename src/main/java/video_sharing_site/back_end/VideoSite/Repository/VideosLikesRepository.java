package video_sharing_site.back_end.VideoSite.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import video_sharing_site.back_end.VideoSite.Entity.VideosLikesEntity;

@Repository
public interface VideosLikesRepository extends JpaRepository<VideosLikesEntity, Long> {
    
}
