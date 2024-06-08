package video_sharing_site.back_end.VideoSite.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import video_sharing_site.back_end.VideoSite.Entity.CategoriesEntity;
import video_sharing_site.back_end.VideoSite.Entity.VideosCategoriesEntity;
import video_sharing_site.back_end.VideoSite.Entity.VideosEntity;

@Repository
public interface VideosCategoriesRepository extends JpaRepository<VideosCategoriesEntity, Long> {
    // video id'sine göre kategorileri bulma
    List<VideosCategoriesEntity> findByVideoId(VideosEntity videoId);

    // kategori id'sine göre videoları bulma
    List<VideosCategoriesEntity> findByCategoryId(CategoriesEntity categoryId);
}
