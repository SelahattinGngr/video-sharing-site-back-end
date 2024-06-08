package video_sharing_site.back_end.VideoSite.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import video_sharing_site.back_end.VideoSite.Entity.CategoriesEntity;

@Repository
public interface CategoriesRepository extends JpaRepository<CategoriesEntity, Integer> {
    // kategori adına göre kategori bulma
    CategoriesEntity findByName(String name);

    // kategori id'sine göre kategori bulma
    CategoriesEntity findById(Long categoryId);
}
