package video_sharing_site.back_end.VideoSite.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;

@Repository
@SuppressWarnings("null")
public interface UsersRepository extends JpaRepository<UsersEntity, Long> {
    // kullanıcı id'sine göre kullanıcı bulma
    Optional<UsersEntity> findById(Long Id);
    
    // kullanıcı adına göre kullanıcı bulma
    UsersEntity findByUsername(String userName);

    // kullanıcı email'ine göre kullanıcı bulma
    UsersEntity findByEmail(String email);

    // kullanıcı refresh token'ına göre kullanıcı bulma
    UsersEntity findByRefreshToken(String refreshToken);

    // kullanıcı adı ve şifresine göre kullanıcı bulma
    UsersEntity findByUsernameAndPassword(String username, String password);

    // kullanıcı email ve şifresine göre kullanıcı bulma
    UsersEntity findByEmailAndPassword(String email, String password);
}
