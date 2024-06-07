package video_sharing_site.back_end.VideoSite.Services.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

/**
 * This class provides token-related services such as token creation, verification, and extracting user information from tokens.
 */
@Service
public class TokenService {

    @Autowired
    private SecretKeysServices secretKeysServices;

    private SecretKey accessSecretToken;
    private SecretKey refreshSecretToken;

    /**
     * Initializes the access and refresh secret tokens using the secret keys obtained from the SecretKeysServices bean.
     */
    @PostConstruct
    public void init() {
        this.accessSecretToken = Keys.hmacShaKeyFor(secretKeysServices.getAccessSecretKey().getBytes());
        this.refreshSecretToken = Keys.hmacShaKeyFor(secretKeysServices.getRefreshSecretKey().getBytes());
    }

    /**
     * Creates a token with the specified email, expiration time, and secret key.
     *
     * @param email      The email associated with the token.
     * @param seconds    The expiration time of the token in seconds.
     * @param secretKey  The secret key used for token encryption.
     * @return           The created token.
     */
    private String createToken(String email, int seconds, SecretKey secretKey) {
        return Jwts.builder()
                .subject(email)
                .expiration(Date.from(new Date().toInstant().plusSeconds(seconds)))
                .signWith(secretKey)
                .compact();
    }

    /**
     * Verifies the authenticity and integrity of a token using the specified secret token and token string.
     *
     * @param secretToken  The secret token used for token verification.
     * @param token        The token string to be verified.
     * @return             The verified token if valid, null otherwise.
     */
    private Jwt<?, ?> verifyToken(SecretKey secretToken, String token) {
        Jwt<?, ?> jwt;
        try {
            jwt = Jwts.parser()
                    .verifyWith(secretToken)
                    .build()
                    .parse(token);
            return jwt;
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * Extracts the user information from a token using the specified secret key and token string.
     *
     * @param secretKey  The secret key used for token verification.
     * @param token      The token string from which to extract the user information.
     * @return           The user information extracted from the token if valid, null otherwise.
     */
    private String getUserFromToken(SecretKey secretKey, String token) {
        try {
            return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Creates an access token for the specified email.
     *
     * @param email  The email associated with the access token.
     * @return       The created access token.
     */
    public String createAccessToken(String email) {
        String accessToken = createToken(email, (60 * 30), accessSecretToken);
        return accessToken;
    }

    /**
     * Creates a refresh token for the specified email.
     *
     * @param email  The email associated with the refresh token.
     * @return       The created refresh token.
     */
    public String createRefreshToken(String email) {
        String refreshToken = createToken(email, (60 * 60 * 24 * 7), refreshSecretToken);
        return refreshToken;
    }

    /**
     * Verifies the authenticity and integrity of an access token.
     *
     * @param token  The access token to be verified.
     * @return       The verified access token if valid, null otherwise.
     */
    public Jwt<?, ?> verifyAccessToken(String token) {
        return verifyToken(accessSecretToken, token);
    }

    /**
     * Verifies the authenticity and integrity of a refresh token.
     *
     * @param token  The refresh token to be verified.
     * @return       The verified refresh token if valid, null otherwise.
     */
    public Jwt<?, ?> verifyRefreshToken(String token) {
        return verifyToken(refreshSecretToken, token);
    }

    /**
     * Extracts the user information from a refresh token.
     *
     * @param token  The refresh token from which to extract the user information.
     * @return       The user information extracted from the refresh token if valid, null otherwise.
     */
    public String getUserFromRefreshToken(String token) {
        return getUserFromToken(refreshSecretToken, token);
    }

    /**
     * Extracts the user information from an access token.
     *
     * @param token  The access token from which to extract the user information.
     * @return       The user information extracted from the access token if valid, null otherwise.
     */
    public String getUserFromAccessToken(String token) {
        return getUserFromToken(accessSecretToken, token);
    }

}
