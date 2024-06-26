package video_sharing_site.back_end.VideoSite.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import video_sharing_site.back_end.VideoSite.Dto.UserDTO;
import video_sharing_site.back_end.VideoSite.Entity.UsersEntity;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserInvalidException;
import video_sharing_site.back_end.VideoSite.Exception.UserExceptions.UserNotFoundException;
import video_sharing_site.back_end.VideoSite.Repository.UsersRepository;
import video_sharing_site.back_end.VideoSite.Shared.Services.PasswordService;
import video_sharing_site.back_end.VideoSite.Shared.Services.jwt.TokenService;

@Service
public class AuthService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private TokenService tokenService;

    public UsersEntity signup(UserDTO user) {
        UsersEntity newUser = new UsersEntity();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setUserName(user.getUserName());
        newUser.setPassword(passwordService.encodePassword(user.getPassword()));
        newUser.setBirthday(user.getBirthday());
        return usersRepository.save(newUser);
    }

    public UsersEntity signin(UserDTO user) {
        UsersEntity userEntity;
        if (user.getEmail() != null)
            userEntity = usersRepository.findByEmail(user.getEmail());
        else
            userEntity = usersRepository.findByUserName(user.getUserName());
        if (!passwordService.matches(user.getPassword(), userEntity.getPassword()))
            throw new UserInvalidException();
        if (userEntity.getRefreshToken() != null)
            redisTemplate.delete(userEntity.getRefreshToken());
        System.out.println("userEntity.getRefreshToken()");
        UserDTO redisDto = entityToDto(userEntity);
        redisTemplate.opsForValue().set(redisDto.getRefreshToken(), redisDto);
        redisTemplate.expire(redisDto.getRefreshToken(), (60 * 60 * 24 * 7), TimeUnit.SECONDS);
        userEntity.setRefreshToken(redisDto.getRefreshToken());
        userEntity.setAccessToken(tokenService.createAccessToken(userEntity.getEmail(), userEntity.getUserName()));
        usersRepository.save(userEntity);
        return userEntity;
    }

    private UserDTO entityToDto(UsersEntity userEntity) {
        Long id = userEntity.getId();
        String username = userEntity.getUserName();
        String email = userEntity.getEmail();
        String firstName = userEntity.getFirstName();
        String lastName = userEntity.getLastName();
        boolean role = userEntity.isRole();
        String password = userEntity.getPassword();
        LocalDate birthday = userEntity.getBirthday();
        String refreshToken = tokenService.createRefreshToken(email, username);
        return new UserDTO(id, role, firstName, lastName, email, username, password, birthday, refreshToken);
    }

    public void signout(String refreshToken) {
        Object object = redisTemplate.opsForValue().get(refreshToken);
        String email = tokenService.getUserFromRefreshToken(refreshToken);
        if (object == null)
            throw new UserInvalidException();
        redisTemplate.delete(refreshToken);
        UsersEntity userEntity = usersRepository.findByEmail(email);
        userEntity.setRefreshToken(null);
        userEntity.setAccessToken(null);
        usersRepository.save(userEntity);
    }

    public Map<String, Object> refreshToken(String refreshToken) {
        String email = tokenService.getUserFromRefreshToken(refreshToken);
        UsersEntity userEntity = usersRepository.findByEmail(email);
        if (userEntity == null)
            throw new UserNotFoundException();
        if (userEntity.getRefreshToken() != null ) /* && !userEntity.getRefreshToken().equals(refreshToken))*/
            redisTemplate.delete(userEntity.getRefreshToken()); // throw new TokenInvalidException();
        String newRefreshToken = tokenService.createRefreshToken(email, userEntity.getUserName());
        String newAccessToken = tokenService.createAccessToken(email, userEntity.getUserName());
        UserDTO redisDto = entityToDto(userEntity);
        redisTemplate.opsForValue().set(redisDto.getRefreshToken(), redisDto);
        redisTemplate.expire(redisDto.getRefreshToken(), (60 * 60 * 24 * 7), TimeUnit.SECONDS);
        userEntity.setRefreshToken(redisDto.getRefreshToken());
        usersRepository.save(userEntity);
        return Map.of("RefreshToken", newRefreshToken, "AccessToken", newAccessToken);
    }
}
