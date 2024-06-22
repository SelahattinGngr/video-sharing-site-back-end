package video_sharing_site.back_end.VideoSite.Repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Entity.VideoCommentsEntity;
import video_sharing_site.back_end.VideoSite.Entity.VideosEntity;

@Repository
public interface VideoCommentsRepository extends JpaRepository<VideoCommentsEntity, Long> {
    // video id'sine göre yorumları bulma
    List<VideoCommentsEntity> findByVideoIdAndStatus(VideosEntity videoId, boolean status, Pageable pageable);

    // kullanıcı id'sine göre yorumları bulma
    List<VideoCommentsEntity> findByUserId(UsersEntity userId);

    // yorum id'sine göre yorum bulma
    VideoCommentsEntity findById(VideoCommentsEntity commentId);

    // yorum id'sine göre yorum bulma
    List<VideoCommentsEntity> findByParentIdAndStatus(VideoCommentsEntity parentId, boolean status);
}
