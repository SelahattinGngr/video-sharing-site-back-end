package video_sharing_site.back_end.VideoSite.Shared.Services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * The PasswordService class provides methods for encoding and matching passwords.
 */
@Service
public class PasswordService {

    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a PasswordService object with a BCryptPasswordEncoder.
     */
    public PasswordService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Encodes the given password using the password encoder.
     *
     * @param password the password to encode
     * @return the encoded password
     */
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Checks if the given raw password matches the encoded password.
     *
     * @param rawPassword     the raw password to check
     * @param encodedPassword the encoded password to compare against
     * @return true if the passwords match, false otherwise
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
