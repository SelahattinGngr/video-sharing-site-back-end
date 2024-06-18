package video_sharing_site.back_end.VideoSite.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import video_sharing_site.back_end.VideoSite.Entity.FollowersEntity;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;

@Repository
public interface FollowersRepository extends JpaRepository<FollowersEntity, Long> {
    // takip edilen kullanıcı id'sine göre takipçileri bulma
    List<FollowersEntity> findByFollowedUserId(UsersEntity usersEntity);

    FollowersEntity findByFollowedUserIdAndFollowingUserId(UsersEntity followedUserId, UsersEntity followingUserId);
}
