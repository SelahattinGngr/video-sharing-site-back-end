package video_sharing_site.back_end.VideoSite.Shared.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void saveAccessToken(String token, String email) {
        redisTemplate.opsForValue().set(token, email, 30, TimeUnit.MINUTES);
    }

    public void saveRefreshToken(String token, String email) {
        redisTemplate.opsForValue().set(token, email, 7, TimeUnit.DAYS);
    }

    public String getEmailFromToken(String token) {
        return redisTemplate.opsForValue().get(token);
    }

    public void deleteToken(String token) {
        redisTemplate.delete(token);
    }
}