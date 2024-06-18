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
import lombok.Data;

@Data
@Entity
@Table(name = "logs")
public class LogsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private UsersEntity userId;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @PrePersist
    public void prePersist() {
        time = LocalDateTime.now();
    }
}
