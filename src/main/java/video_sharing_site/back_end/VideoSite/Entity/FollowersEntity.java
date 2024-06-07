package video_sharing_site.back_end.VideoSite.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "followers")
public class FollowersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Takip edilen kullanıcı id'si
    @ManyToOne
    @JoinColumn(name = "followed_user_id", nullable = false)
    private UsersEntity followedUserId;

    // Takip eden kullanıcı id'si
    @ManyToOne
    @JoinColumn(name = "following_user_id", nullable = false)
    private UsersEntity followingUserId;

    @Column(name = "created_ad")
    private LocalDateTime createdAd;

    @PrePersist
    public void onPrePersist() {
        this.createdAd = LocalDateTime.now();
    }

}
