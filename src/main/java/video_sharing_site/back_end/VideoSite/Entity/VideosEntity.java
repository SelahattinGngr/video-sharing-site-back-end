package video_sharing_site.back_end.VideoSite.Entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "videos")
public class VideosEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "url", nullable = false, unique = true)
    private String url;

    @Column(name = "thumbnail", nullable = false)
    private String thumbnail;

    @Column(name = "views", nullable = false)
    private long views;

    @Column(name = "like_count", nullable = false)
    private long likeCount;

    @Column(name = "dislike_count", nullable = false)
    private long dislikeCount;

    @Column(name = "status")
    private boolean status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "uploaded_user_id", nullable = false)
    private UsersEntity uploadedUserId;

    @ManyToMany
    @JoinTable(name = "videos_likes", joinColumns = @JoinColumn(name = "video_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UsersEntity> likes;

    @ManyToMany
    @JoinTable(name = "video_categories", joinColumns = @JoinColumn(name = "video_id"), inverseJoinColumns = @JoinColumn(name = "categorie_id"))
    private List<CategoriesEntity> categories;

    @PrePersist
    public void onCreate() {
        this.views = 0;
        this.likeCount = 0;
        this.dislikeCount = 0;
        this.status = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
