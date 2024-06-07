package video_sharing_site.back_end.VideoSite.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "video_comments_likes")
public class VideoCommentsLikesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private VideoCommentsEntity commentId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UsersEntity userId;
}
