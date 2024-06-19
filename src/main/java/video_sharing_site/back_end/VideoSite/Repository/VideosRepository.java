package video_sharing_site.back_end.VideoSite.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import video_sharing_site.back_end.VideoSite.Entity.VideosEntity;

@Repository
@SuppressWarnings("null")
public interface VideosRepository extends JpaRepository<VideosEntity, Long> {
    // video id'sine göre video bulma
    Optional<VideosEntity> findById(Long videoId);

    // video adına göre video bulma
    VideosEntity findByTitle(String title);

    // video url'sine göre video bulma
    VideosEntity findByUrl(String videoUrl);

    // video status'üne göre video bulma ve sayfa sayısı
    List<VideosEntity> findByStatus(boolean status, Pageable page);
}
