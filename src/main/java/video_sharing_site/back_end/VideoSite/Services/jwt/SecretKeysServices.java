package video_sharing_site.back_end.VideoSite.Services.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * This service class provides access to secret keys used for JWT authentication in the CrySus server.
 */
@Service
public class SecretKeysServices {

    @Value("${video-site.server.jwt.secret.access.key}")
    private String accessSecretKey;

    @Value("${video-site.server.jwt.secret.refresh.key}")
    private String refreshSecretKey;

    /**
     * Retrieves the access secret key used for JWT authentication.
     * 
     * @return The access secret key.
     */
    public String getAccessSecretKey() {
        return accessSecretKey;
    }

    /**
     * Retrieves the refresh secret key used for JWT authentication.
     * 
     * @return The refresh secret key.
     */
    public String getRefreshSecretKey() {
        return refreshSecretKey;
    }
}
