package video_sharing_site.back_end.VideoSite.Entity;

import jakarta.persistence.Column;
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
@Table(name = "playlist_videos")
public class PlaylistVideosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "playlist_id", nullable = false)
    private PlaylistsEntity playlistId;

    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false)
    private VideosEntity videoId;

    @Column(name = "video_order")
    private int order;
}
